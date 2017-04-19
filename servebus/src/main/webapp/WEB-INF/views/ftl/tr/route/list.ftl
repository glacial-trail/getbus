<h2><@spring.message "cabinet.partner.route.routeList"/></h2>

<@createLnk/>

<#list routes>
<#--TODO sold tickets last 45 days-->
    <table>
        <tr>
            <th>tickets sold</th>
            <th>name</th>
            <th>start point</th>
            <th>end point</th>
            <th>buttons</th>
        </tr>
        <tbody>
            <#items as route>
            <tr>
                <td><#--${route.forwardSold}--></td>
                <td>${route.name}</td>
                <td>${route.startPoint}</td>
                <td>${route.endPoint}</td>
                <td rowspan="2">
                    <a href="view/${route.id}">view</a> <br/>
                    <#if route.editable>
                        <a href="edit/${route.id}">edit</a>
                    </#if>
                </td>
            </tr>
            <tr>
                <td><#--${route.reverseSold}--></td>
                <td>${route.name}</td>
                <td>${route.endPoint}</td>
                <td>${route.startPoint}</td>
            </tr>
            </#items>
        </tbody>
    </table>
    <@createLnk/>
</#list>

<#macro createLnk>
    <a href="create"><@spring.message "cabinet.partner.route.addRoute"/></a>
</#macro>