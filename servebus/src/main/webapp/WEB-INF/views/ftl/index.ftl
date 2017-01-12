<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>GetBus</title>
</head>
<body>

<div>
    <h1>GetBus.info</h1>
    <p class="lead"><@spring.message "index.lead"/></p>
    <p>Your name: ${name}</p>
    <p>
        roles:
            <#list roles as role>
            ${role}<#sep>,
            </#list>
    </p>

    <br/>

    <table>
        <tr><th> a </th> <th> b </th></tr>
        <#list bl as b>
            <tr>
                <td>${b.a}</td>
                <td>${b.b}</td>
            </tr>
        </#list>
    </table>

    <p><a href="/logout">>logout</a></p>
    <div>
        <p>&copy; GetBus 2016</p>
    </div>
</div>
</body>
</html>
