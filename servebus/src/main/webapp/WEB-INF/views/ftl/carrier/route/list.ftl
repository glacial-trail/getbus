<h2><@spring.message "dashboard.partner.route.routeList"/></h2>

<@createLnk/>

<#list routes>
<#--TODO sold tickets last 45 days-->
    <table class="table table-bordered">
        <tr>
            <th>tickets sold</th>
            <th>name</th>
            <th>start stop</th>
            <th>end stop</th>
            <th>buttons</th>
        </tr>
        <tbody>
            <#items as route>
            <tr>
                <td><#--${route.forwardSold}--></td>
                <td>${route.name}</td>
                <td>${route.startStop}</td>
                <td>${route.endStop}</td>
                <td rowspan="2">
                    <a href="view/${route.id}">view</a> <br/>
                    <#if route.editable>
                        <a href="edit/${route.id?c}">edit</a> <br/>
                        <a href="${route.id?c}/periodicity">periodicity</a><#--TODO make 'create periodicity/edit periodicity'-->
                    </#if>
                </td>
            </tr>
            <tr>
                <td><#--${route.reverseSold}--></td>
                <td>${route.name}</td>
                <td>${route.endStop}</td>
                <td>${route.startStop}</td>
            </tr>
            </#items>
        </tbody>
    </table>
    <@createLnk/>
</#list>

<#macro createLnk>
    <a href="create"><@spring.message "dashboard.partner.route.addRoute"/></a>
</#macro>