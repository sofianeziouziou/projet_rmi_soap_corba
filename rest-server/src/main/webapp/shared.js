const apiUsers = "http://localhost:8080/users";
const apiBiens = "http://localhost:8080/biens";
const jmsApi = "http://localhost:8080/notifications"; // REST pour JMS
const soapUrl = "http://localhost:8081/contracts?wsdl";

let currentUser = localStorage.getItem("currentUser") ? JSON.parse(localStorage.getItem("currentUser")) : null;

function showMessage(id,msg,type='error'){
    const el = document.getElementById(id);
    if(el){ el.innerHTML=`<div class="message ${type}">${msg}</div>`; setTimeout(()=>el.innerHTML='',4000);}
}

// --- LOGIN ---
async function sharedLogin(){
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    if(!email||!password){ showMessage('login-message','Veuillez remplir tous les champs'); return; }
    try{
        const res = await fetch(`${apiUsers}/login`,{
            method:'POST', headers:{'Content-Type':'application/json'},
            body:JSON.stringify({email,password})
        });
        if(res.ok){
            const user = await res.json();
            localStorage.setItem("currentUser", JSON.stringify(user));
            window.location.href="biens.html";
        } else { showMessage('login-message','Email ou mot de passe incorrect'); }
    }catch(e){ showMessage('login-message','Erreur serveur'); }
}

// --- REGISTER ---
async function register(){
    const nom=document.getElementById('register-nom').value;
    const email=document.getElementById('register-email').value;
    const password=document.getElementById('register-password').value;
    const role=document.getElementById('register-role').value;
    if(!nom||!email||!password){ showMessage('register-message','Veuillez remplir tous les champs'); return; }
    try{
        const res = await fetch(`${apiUsers}/register`,{
            method:'POST', headers:{'Content-Type':'application/json'},
            body:JSON.stringify({nom,email,password,role})
        });
        if(res.ok){
            showMessage('register-message','Inscription réussie ! Redirection...', 'success');
            setTimeout(()=>window.location.href="login.html",1500);
        } else {
            const err = await res.json();
            showMessage('register-message',err.error==='exists'?'Cet email est déjà utilisé':'Erreur serveur');
        }
    }catch(e){ showMessage('register-message','Erreur serveur'); }
}

// --- LOGOUT ---
function logout(){
    localStorage.removeItem("currentUser");
    window.location.href="login.html";
}

// --- LOAD BIENS ---
async function loadBiens(){
    try{
        const res = await fetch(apiBiens);
        const container = document.getElementById('biens-list');
        if(res.ok){
            const biens = await res.json();
            container.innerHTML='<h3>Total: '+biens.length+' biens</h3>';
            biens.forEach(b=>{
                const div=document.createElement('div');
                div.className='user-card';
                let dispo = b.disponible ? 'Oui' : 'Non';
                div.innerHTML=`<strong>${b.titre}</strong><br>${b.description}<br>Prix: ${b.prix}<br>Disponible: ${dispo}`;
                if(currentUser.role==='acheteur' && b.disponible){
                    div.innerHTML+=`<br><button class="action" onclick="acheter(${b.id})">Acheter</button>`;
                }
                container.appendChild(div);
            });
        } else { container.innerHTML='Erreur chargement'; }
    }catch(e){ document.getElementById('biens-list').innerHTML='Erreur serveur'; }
}

// --- ADD BIEN (AGENT) ---
async function addBien(){
    const titre=document.getElementById('bien-titre').value;
    const description=document.getElementById('bien-description').value;
    const prix=parseFloat(document.getElementById('bien-prix').value);
    if(!titre || !prix){ alert("Titre et prix requis"); return; }
    try{
        const res = await fetch(apiBiens,{
            method:'POST',
            headers:{'Content-Type':'application/json'},
            body: JSON.stringify({titre, description, prix, agentId:currentUser.id, disponible:true})
        });
        if(res.ok){
            alert("Bien ajouté !");
            notifyAll(`Nouveau bien ajouté : ${titre}`);
            loadBiens();
        } else { alert("Erreur ajout"); }
    }catch(e){ alert("Erreur serveur"); }
}

// --- ACHETER BIEN ---
async function acheter(bienId){
    try{
        // SOAP contract
        const xmlBody=`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:con="http://soap.immobilier.com/">
           <soapenv:Header/>
           <soapenv:Body>
              <con:generateContract>
                 <bienId>${bienId}</bienId>
                 <acheteurId>${currentUser.id}</acheteurId>
              </con:generateContract>
           </soapenv:Body>
        </soapenv:Envelope>`;

        const res = await fetch("http://localhost:8081/contracts",{
            method:'POST',
            headers:{'Content-Type':'text/xml'},
            body: xmlBody
        });

        const text = await res.text();
        if(text.includes("<contract>")){
            alert("Achat réussi !\n"+text);
            notifyAll(`Bien acheté : ID ${bienId} par ${currentUser.nom}`);
            loadBiens();
        } else alert("Erreur lors de la création du contrat");
    }catch(e){ alert("Erreur serveur"); }
}

// --- JMS Notification via REST ---
async function notifyAll(message){
    try{
        await fetch(jmsApi,{
            method:'POST',
            headers:{'Content-Type':'application/json'},
            body: JSON.stringify({message})
        });
    }catch(e){ console.error("Erreur notification"); }
}

// --- LISTEN NOTIFICATIONS (polling simple) ---
async function listenNotifications(){
    const notifContainer = document.getElementById('notifications');
    setInterval(async ()=>{
        try{
            const res = await fetch(jmsApi);
            if(res.ok){
                const notifs = await res.json();
                notifContainer.innerHTML='';
                notifs.forEach(n=>{
                    const div = document.createElement('div');
                    div.className='user-card';
                    div.innerText = n.message;
                    notifContainer.appendChild(div);
                });
            }
        }catch(e){}
    },5000);
}
