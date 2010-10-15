<html>
<head>
    <title>$messages.formTitle$ $themeMessages.("theme.suffix")$</title>
</head>
<body class="form">
<form id="testForm" action="$servlet$$formAction$" method="get">
    <table>
        $formRow(id="name", name=messages.("name.key"), status=bindStatus.("command.name"))$
        $formRow(id="number", name=messages.("number.key"), status=bindStatus.("command.number"))$
        <tr>
            <td colspan="2">
                <input type="submit" value="$messages.submitButton$">
                <a href="$servlet$/reset">$messages.resetLink$</a>
            </td>
        </tr>
    </table>
    <p>$messages.(["message.with.arguments", arg1, "? & ?", arg2])$</p>
    <p>$messages.("unknown.message.code")$</p>
</form>
</body>
</html>
