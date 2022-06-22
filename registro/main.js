const signupForm = document.querySelector('#formulario');


signupForm.addEventListener('submit',(e)=>
{
	e.preventDefault();

	const correo = document.querySelector('#correo').value
	const contra = document.querySelector('#contra').value

	console.log(correo,contra);

	firebase.auth()
		createUserWithEmailAndPassword(correo,contra)
		.then(userCredential =>
		{
			console.log('listo')
		})

})