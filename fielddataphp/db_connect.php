<?php
   $host        = "host=127.0.0.1";
   $port        = "port=8080";
   $dbname      = "dbname=postgres";
   $credentials = "user=postgres password=root";

   $db = pg_connect( "$host $port $dbname $credentials"  );
   if(!$db){
      echo "Error : Unable to open database\n";
   } else {
      echo "Opened database successfully\n";
   }
?>
