<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>EFT Transactions</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="./css/base-min.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="./css/pure-min.css" type="text/css">
</head>
<body>
<form class="pure-form" action="testQueryEFTdata" method="post">
    <fieldset>
        <legend>Query EFT Transactions</legend>

        <input name="startTime" type="text" placeholder="like 2018-01-09 17:00:00">

        <button type="submit" class="pure-button pure-button-primary">commit</button>
    </fieldset>
</form>
</body>
</html>
