<script type="text/javascript">
    function addRoutePoint(button) {
        $(button).parent().after($("#route-point-template div").clone());
        reindexRoutePoints();
        return false;
    }

    function removeRoutePoint(button) {
        $(button).parent().remove();
        reindexRoutePoints();
        return false;
    }

    function reindexRoutePoints() {
        $("form[name=route] fieldset.route-point").each(function (idx) {
            var elem = $(this);
            if (elem.data("rp-idx") != idx) {
                elem.attr("data-rp-idx", idx);
                elem.find("input").each(function () {
                    var input = $(this);
                    var newName = input.attr("name").replace(/(routePoints\[)(-?\d+)(\])/, "$1"+idx+"$3");
                    input.attr("name", newName);
                })
            }
        });
    }
</script>

<div style="display: none;" id="route-point-template">
    <@routepoint idx=-1 />
</div>

<form name="route" action="create-route" method="post">
    <input name="id" type="hidden" value="${route.id!''}"/>
    <input name="direction" value="${route.direction}" type="hidden"/>
    <input name="name" value="${route.name!''}"/>
    <br/>
    <br/>
    <#list route.routePoints as routePoint>
        <@routepoint idx=routePoint?index rp=routePoint
            add='F' == route.direction && !routePoint?is_first
            remove='F' == route.direction && !(routePoint?is_first||routePoint?is_last)
        />
    <#else>
        <@routepoint idx="0" remove=false />
        <@routepoint idx="1" add=false remove=false />
    </#list>

    <br/>
    <input type="submit"/>
</form>

<#macro routepoint idx rp={} add=true remove=true >
    <div>
        <fieldset class="route-point" data-rp-idx="${idx}">
            <input name="routePoints[${idx}].id" value="${rp.id!''}" type="hidden"/>
            <input name="routePoints[${idx}].name" value="${rp.name!''}"/>
            <input name="routePoints[${idx}].address" value="${rp.address!''}"/>
            <input name="routePoints[${idx}].arrival" value="${rp.arrival!''}"/>
            <input name="routePoints[${idx}].departure" value="${rp.departure!''}"/>
            <input name="routePoints[${idx}].tripTime" value="${rp.tripTime!''}"/>
            <input name="routePoints[${idx}].distance" value="${rp.distance!''}"/>
        </fieldset>
        <#if add>
            <a href="#" onclick="addRoutePoint(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
        </#if>
        <#if remove>
            <a href="#" onclick="removeRoutePoint(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
        </#if>
    </div>
</#macro>