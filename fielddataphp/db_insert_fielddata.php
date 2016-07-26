<?php
   $host        = "host=127.0.0.1";
   $port        = "port=5432";
   $dbname      = "dbname=fielddata";
   $credentials = "user=postgres password=root";
   $db = pg_connect( "$host $port $dbname $credentials"  );
   if($db){

//$data = file_get_contents("php://input");
//$data=str_replace('\\', '', $data);
$results=array();
$results=json_decode(file_get_contents("php://input"),true);
$myfile = fopen("newfile.txt", "w");
$query=pg_prepare($db, "my_query", 'INSERT INTO fielddata (image,video, latitude, longitude,label,category) VALUES($1,$2,$3,$4,$5,$6)');
//fwrite($myfile,$data);

foreach($results[results] as $row) {
$image = $row[image];
//fwrite($myfile,$image);
$video = $row[video];
//fwrite($myfile,$video);
$latitude = $row[latitude];
//fwrite($myfile,$latitude);
$longitude = $row[longitude];
//fwrite($myfile,$longitude);
$label = $row[text];
//fwrite($myfile,$label);
$category = $row[category];
//fwrite($myfile,$category);
//pg_query('INSERT INTO fielddata (image,video, latitude, longitude,label,category) VALUES($image,$video,$latitude,$longitude,$label,$category)');
pg_execute($db, "my_query", array($image,$video,$latitude,$longitude,$label,$category));
fwrite($myfile, pg_last_error($db));
}
fclose($myfile);
pg_close($db);
}
?>
