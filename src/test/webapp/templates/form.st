<html>
<head>
    <title>$messages.formTitle.to_s$ $themeMessages.("theme.suffix").to_s$</title>
</head>
<body class="form">
<form id="testForm" action="$servlet$$formAction$" method="get">
    <table>
        $formRow(id="name", name=messages.("name.key").to_s, status=bindStatus.("command.name"))$
        $formRow(id="number", name=messages.("number.key").to_s, status=bindStatus.("command.number"))$
        <tr>
            <td colspan="2">
                <input type="submit" value="$messages.submitButton.to_s$">
                <a href="$servlet$/reset">$messages.resetLink.to_s$</a>
            </td>
        </tr>
    </table>
    <p>$messages.("message.with.arguments").(arg1).("? & ?").(arg2).to_s$</p>
    <p>$messages.("unknown.message.code").to_s$</p>
</form>
</body>
</html>
