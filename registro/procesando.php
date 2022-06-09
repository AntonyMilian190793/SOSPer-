<?php
	$nombres = $_POST['nombres'];
	$apellidos = $_POST['apellidos'];
	$dni = $_POST['dni'];
	$correo = $_POST['correo'];
	$contra = $_POST['contra'];

	echo $nombres;
	echo $apellidos;
	echo $dni;
	echo $correo;
	echo $contra;

	$data = '{"email":"david@gmail.com","name":"David Flores"}';
	$url = "https://viajeseguro-48827-default-rtdb.firebaseio.com/Users/Drivers.json";
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
	curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: text/plain'));

	$response = curl_exec($ch);
	if( curl_errno($ch) ){
		echo 'Error: '.curl_errno($ch);
	}
	else
	{
		echo 'Ya inserto';
	}
?>