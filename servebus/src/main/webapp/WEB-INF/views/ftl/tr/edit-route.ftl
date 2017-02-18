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
    <div>
        <fieldset class="route-point" data-rp-idx="-1">
            <input name="routePoints[-1].id" type="hidden"/>
            <input name="routePoints[-1].name"/>
            <input name="routePoints[-1].address"/>
            <input name="routePoints[-1].arrival"/>
            <input name="routePoints[-1].departure"/>
            <input name="routePoints[-1].tripTime"/>
            <input name="routePoints[-1].distance"/>
        </fieldset>
        <a href="#" onclick="addRoutePoint(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
        <a href="#" onclick="removeRoutePoint(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
    </div>
</div>

<form name="route" action="create-route" method="post">
    <input name="id" type="hidden"/>
    <input name="direction" value="F" type="hidden"/>
    <label for="name">Please, enter route name</label>
    <input name="name" id="name" class="route_name" placeholder="enter route name field" required/>
    <br/>
    <br/>

    <div>
        <fieldset class="route-point" data-rp-idx="0">
            <input name="routePoints[0].id" type="hidden"/>
            <input name="routePoints[0].name" type="text" placeholder="Start station name"/>
            <input name="routePoints[0].address" type="text" placeholder="Address"/>
            <input name="routePoints[0].arrival"/>
            <input name="routePoints[0].departure"/>
            <input name="routePoints[0].tripTime" type="text" placeholder="Trip time"/>
            <input name="routePoints[0].distance" type="text" placeholder="Distance"/>
        </fieldset>
        <a href="#" onclick="addRoutePoint(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
        <a href="#" onclick="removeRoutePoint(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
    </div>

    <div>
        <fieldset class="route-point" data-rp-idx="1">
            <input name="routePoints[1].id" type="hidden"/>
            <input name="routePoints[1].name"/>
            <input name="routePoints[1].address"/>
            <input name="routePoints[1].arrival"/>
            <input name="routePoints[1].departure"/>
            <input name="routePoints[1].tripTime"/>
            <input name="routePoints[1].distance"/>
        </fieldset>
        <a href="#" onclick="addRoutePoint(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
        <a href="#" onclick="removeRoutePoint(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
    </div>

    <input type="submit"/>
</form>
