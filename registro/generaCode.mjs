
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.8.2/firebase-app.js";
import { getAuth, createUserWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/9.8.2/firebase-auth.js";
import { getDatabase, ref, set } from "https://www.gstatic.com/firebasejs/9.8.2/firebase-database.js";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyCPu6AhVHfXkTh0sa2IXms3-6RsYPfUN7I",
  authDomain: "viajeseguro-48827.firebaseapp.com",
  projectId: "viajeseguro-48827",
  databaseURL: "https://viajeseguro-48827-default-rtdb.firebaseio.com",
  storageBucket: "viajeseguro-48827.appspot.com",
  messagingSenderId: "1023996501305",
  appId: "1:1023996501305:android:859447ecdaf0647fc61ce8"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
 
var conteo = 0;
 
conteo++;
const database = getDatabase(app);
set(ref(database, 'codePolicy/' + conteo), {
    code: '123456',
    expira: '10/12/2022'
}); 