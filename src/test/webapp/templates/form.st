<html>
<head>
    <title>Form</title>
</head>
<body>
<form id="testForm" action="$servlet$/form" method="get">
    <table>
        $templates/formRow(id="name", name="Name", status=bindStatus.("command.name"))$
        $templates/formRow(id="number", name="Number", status=bindStatus.("command.number"))$
        <tr>
            <td colspan="2"><input type="submit" value="Submit"></td>
        </tr>
    </table>
</form>
</body>
</html>
