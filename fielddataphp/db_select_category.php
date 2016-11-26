<?php
   $host        = "host=127.0.0.1";
   $port        = "port=5432";
   $dbname      = "dbname=fielddata";
   $credentials = "user=postgres password=root";

   $db = pg_connect( "$host $port $dbname $credentials"  );
   if($db){

   $sql ="SELECT * from CATEGORY";

   $ret = pg_query($db, $sql);
  // if(!$ret){
  //    echo pg_last_error($db);
  //    exit;
  //}
   $arr=array();
   while($row = pg_fetch_assoc($ret)){
     $arr[]=$row;
     }
   echo json_encode($arr);
   //$fp = fopen('categorydata.json', 'w');
   //fwrite($fp, json_encode($arr));
   //fclose($fp);
   }
   pg_close($db);
?>
