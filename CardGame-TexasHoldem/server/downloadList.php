<?php
$out=array();
foreach (glob("*.png") as $filename) {
    $out[]=$filename;
}
echo implode(",", $out);