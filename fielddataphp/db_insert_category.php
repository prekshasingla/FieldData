<?php
   $host        = "host=127.0.0.1";
   $port        = "port=5432";
   $dbname      = "dbname=fielddata";
   $credentials = "user=postgres password=root";

   $db = pg_connect( "$host $port $dbname $credentials"  );
   if($db){
      $sql =
      //"INSERT INTO CATEGORY (ID,NAME,LABELS)
      //VALUES (1, 'Category1', 'Label1.1,Label1.2' )";
      //"INSERT INTO CATEGORY (ID,NAME,LABELS)
      //VALUES (2, 'Category2', 'Label2.1,Label2.2' )";
      //"INSERT INTO CATEGORY (ID,NAME,LABELS)
      //VALUES (3, 'Category3', 'Label3.1,Label3.2' )";
      "INSERT INTO CATEGORY (ID,NAME,LABELS)
      VALUES (4, 'Category4', 'Label4.1,Label4.2' )";


   $ret = pg_query($db, $sql);

 }
   pg_close($db);
?>
