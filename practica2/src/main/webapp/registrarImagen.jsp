<%-- 
    Document   : registrarImagen
    Created on : 20 sept 2024, 10:41:58
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Image</title>
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    </head>
   <body>
        <div class="wrapper">
            <h1>Upload an Image</h1>
            <!-- Es necesario añadir enctype="multipart/form-data" para poder subir archivos-->
            <form action="registrarImagen" method = "POST" enctype="multipart/form-data">
                <!-- Identificador de imagen, generado automaticamente (primary key) -->
                <div class="input-box">
                    <input type="text" name="title" placeholder="Image title" required>
                </div> 
                <div class="input-box">
                    <input type="text" name="descp" placeholder="Image description" required>
                </div>
                <div class="input-box">
                    <input type="text" name="keyw" placeholder="Key words for the image" required>
                </div>
                <div class="input-box">
                    <input type="text" name="ath" placeholder="Image author" required>
                </div>
                <!-- Creador (usuario que hace upload) -->
                <div class="input-box">
                    <input type="date" name="cdate" placeholder="Creation date" required>
                </div>
                <!-- Date of upload -->
                <div class="selector">
                    <input type="file" name="file" required/>
                </div>
                <div class="btn">
                    <button type="submit">Upload</button>
                </div>
            </form>
        </div>
    </body>
</html>
