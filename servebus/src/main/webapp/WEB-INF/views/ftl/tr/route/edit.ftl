<#-- @ftlvariable name="countries" type="java.util.List<String>" -->
<#-- @ftlvariable name="route" type="info.getbus.servebus.model.route.Route" -->
<#-- @ftlvariable name="viewMode" type="boolean" -->

<#import "../../macro/forms.ftl" as form>

<@spring.bind "route" />

<#--<#assign readonlyMode = false />-->
<#assign isReverseRoute = route.direction == 'R' />
<#--<#assign restrictedit = route.direction == 'R' />-->
<#assign wpEditable = !viewMode && route.direction == 'F' />

<script type="text/javascript">
    const combodateConf = {
        format: 'HH:mm',
        template: 'HH : mm',
        value: '00:00',
        firstItem: 'none',
        minuteStep: 1
    };
    const autocompleteOpts = {
        componentRestrictions: {
            country: ['${countries?join("','")}'] //Warn. Up tu 5 countries
        }
    };

    function submitForm(btn) {
        btn = $(btn);
        let url = btn.attr('formaction');
        btn.closest('form').attr('action', url).submit()
    }

    $(function() {
        $('.time:not(#route-point-template .time)').combodate(combodateConf);
    });

<#if !isReverseRoute && !viewMode>
    function addRoutePoint(button) {
        let rowWithButton = $(button).parent().parent();
        let routePointTemplate = $("#route-point-template");
        rowWithButton.after(routePointTemplate.find("tr:nth-child(2)").clone());
        let inputsRow = routePointTemplate.find("tr:first-child").clone();
        inputsRow.attr("data-new-row-marker","this-is-new");
        rowWithButton.after(inputsRow);
        reindexRoutePoints();
        inputsRow.find('.time').combodate(combodateConf);
        inputsRow = $("tr.route-point[data-new-row-marker='this-is-new']");
        inputsRow.removeAttr("data-new-row-marker");
        let addressInput = inputsRow.find('input.address-autocomplete')[0];
        attachAutocomplete(addressInput/*, inputsRow.attr('data-wp-idx')*/);
        return false;
    }

    function removeRoutePoint(button) {
        $(button).parent().parent().prev().remove();
        $(button).parent().parent().remove();
        reindexRoutePoints();
        return false;
    }

    function reindexRoutePoints() {
        $("form[name=route] tr.route-point").each(function (idx) {
            let elem = $(this);
            if (elem.data("wp-idx") !== idx) {
                elem.attr("data-wp-idx", idx);
                elem.find(".route-data:input").each(function () {
                    let input = $(this);
                    let newName = input.attr("name").replace(/(wayPoints\[)(-?\d+)(\])/, "$1" + idx + "$3");
                    input.attr("name", newName);
                })
            }
        });
    }

    function attachAutocomplete(elm) {
        let autocomplete = new google.maps.places.Autocomplete(elm, autocompleteOpts);
        autocomplete.addListener('place_changed', function () {
            let place = autocomplete.getPlace();
            let handler = new AddressComponentHandler(place);
            fillInAddressFields(wpIdx(elm), handler);
        });
    }

    function initAutocomplete() {
        $('.address-autocomplete:not(#route-point-template .address-autocomplete)').each(function (i, elm) {
            attachAutocomplete(elm);
        });
    }
    
    function wpIdx(someWpElem) {
        return $(someWpElem).closest('tr').attr("data-wp-idx");
    }

    class AddressComponentHandler {
        constructor(place) {
            let components = place.address_components;
            if (components) {
                components.forEach((elm) => {
                    let addressType = elm.types[0];
                    if (this[addressType]) {
                        this[addressType](elm);
                    }
                });
                this.street = [this.streetName, this.streetNumber].join(" ").trim();
            }
            this.utcOffset = place.utc_offset;
        }
        street_number(addressComponent) { this.streetNumber = addressComponent.long_name; }
        route(addressComponent) { this.streetName = addressComponent.long_name; }
        locality(addressComponent) { this.city = addressComponent.long_name; }
        administrative_area_level_1 (addressComponent) { this.region = addressComponent.long_name; }
        country(addressComponent) { this.countryCode = addressComponent.short_name; }
        postal_code(addressComponent) { this.zip = addressComponent.short_name; }
    }

    function fillInAddressFields(idx, address) {
        component("address").val(address.street);
        component("city").val(address.city);
        component("zip").val(address.zip);
        component("region").val(address.region);
        component("countryCode").val(address.countryCode);
        component("utcOffset").val(address.utcOffset);

        function component(name) {
            return $(`:input[name='wayPoints[${r"${idx}"}].${r"${name}"}']`);
        }
    }
</#if>
</script>
<#if !isReverseRoute && !viewMode>
<script
        src="https://maps.googleapis.com/maps/api/js?language=${.lang}&key=AIzaSyBONWRRnzYQe0Zk19ChzzOWfJTEPGDehyg&libraries=places&callback=initAutocomplete"
        async defer></script>
</#if>

<#if !isReverseRoute && !viewMode>
    <table style="display: none;" id="route-point-template">
        <@routepoint idx=-1 />
    </table>
</#if>

<form name="route" action="create-route" method="post" id="data">
    <@hidden name="id" value="${(route.id?c)!''}" />
    <@hidden name="direction" value="${route.direction}" />
    <label for="name">Please, enter route name</label>
    <@text name="name" value="${route.name!''}" id="name" class="route_name"/>
    <@form.showFieldErrors 'name' 'error' />
    <br/>
    <label for="base-price">base price</label>
    <@text name="basePrice" value="${route.basePrice!''}" id="base-price"/>
    <label for="base-seats-qty">base seats qty</label>
    <@number name="baseSeatsQty" value="${route.baseSeatsQty!''}" id="base-seats-qty"/>
    <label for="start-sales">start sales(voyage generation)</label>
    <@date name="startSales" value="${route.startSales!''}" id="start-sales"/>
    <label for="sales-depth">sales(voyage) depth</label>
    <@number name="salesDepth" value="${route.salesDepth!''}" id="sales-depth"/>
    <#--<input type="text" value="end sales(voyage generation, sales depth)">-->

    <br/>
    <br/>
    <table class="table table-bordered table-fixed">
        <thead>
            <tr>
                <th class="col-xs-2"><@spring.message "route.create.Country"/></th>
                <th class="col-xs-2"><@spring.message "route.create.addstation"/></th>
                <th class="col-xs-2"><@spring.message "route.create.address"/></th>
                <th class="col-xs-2"><@spring.message "route.create.depttime"/></th>
                <th class="col-xs-2"><@spring.message "route.create.arrtime"/></th>
                <th class="col-xs-1"><@spring.message "route.create.triptime"/></th>
                <th class="col-xs-1"><@spring.message "route.create.distance"/></th>
            </tr>
        </thead>
    <#list route.wayPoints as wayPoint>
        <@routepoint idx=wayPoint?index rp=wayPoint
            add=!wayPoint?is_last
            remove=!(wayPoint?is_first||wayPoint?is_last)
        />
    <#else>
        <@routepoint idx="0" remove=false />
        <@routepoint idx="1" add=false remove=false />
    </#list>
    <tr>
        <td colspan="7" class="col-xs-12">
        <br/>
        <#if !viewMode>
            <button type="reset">reset</button>
            <@btn formaction="cancel">cancel</@btn>
        </#if>
        <#if viewMode>
            <#if isReverseRoute>
                <a href="${route.id?c}?direction=F">back</a>
            <#else>
                <a href="${route.id?c}?direction=R">next</a>
            </#if>
        <#else>
            <#if isReverseRoute>
                <@btn formaction="back">back</@btn>
            <#else>
                <@btn formaction="save">next</@btn>
                <#--<button type="submit" formaction="save">next</button>-->
            </#if>
            <@btn formaction="save?finish=true">finish</@btn>
        </#if>
        </td>
    </tr>
    </table>
</form>

<#macro routepoint idx rp={} add=true remove=true >
    <tr class="route-point" data-wp-idx="${idx}">
        <input name="wayPoints[${idx}].id" value="${(rp.id?c)!''}" type="hidden" class="route-data"/>

        <td class="col-xs-2">
                <#if viewMode>
                    <span><@spring.message 'country.'+rp.countryCode /></span>
                <#elseif isReverseRoute>
                    <input name="wayPoints[${idx}].countryCode" value="${rp.countryCode!''}" type="hidden" class="route-data"/>
                    <input value="<@spring.message 'country.'+rp.countryCode />" readonly type="text"/>
                <#else>
                    <select name="wayPoints[${idx}].countryCode" class="route-data" id="country">
                        <#list countries as code>
                            <option ${(rp.countryCode?? && code == rp.countryCode)?then('selected','')} value="${code}"><@spring.message 'country.'+code /></option>
                        </#list>
                    </select>
                </#if>
        <#--<@form.showFieldErrors 'wayPoints[${idx}].countryCode' 'error'/>-->
        </td>
        <td class="col-xs-2">
            <@text name="wayPoints[${idx}].name" value=rp.name!"" ro=isReverseRoute <#--id="station"-->/>
            <@form.showFieldErrors 'wayPoints[${idx}].name' 'error'/>
        </td>
        <td class="col-xs-2 address">
            <@hidden name="wayPoints[${idx}].utcOffset" value=rp.utcOffset!"" />
            <@hidden name="wayPoints[${idx}].zip" value=rp.zip!"" />
            <@text name="wayPoints[${idx}].region" value=rp.region!"" class="region" ro=true />
            <@text name="wayPoints[${idx}].city" value=rp.city!"" class="city" ro=true />
            <@text id="wayPoints[${idx}].address" name="wayPoints[${idx}].address" value="${rp.address!''}" ro=isReverseRoute class="address-autocomplete" />
            <@form.showFieldErrors 'wayPoints[${idx}].address' 'error'/>
        </td>
        <td class="col-xs-2"><@time name="wayPoints[${idx}].arrival" value="${rp.arrival!''}" /></td>
        <td class="col-xs-2"><@time name="wayPoints[${idx}].departure" value="${rp.departure!''}" /></td>
        <td class="col-xs-1"><@text name="wayPoints[${idx}].tripTime" value="${rp.tripTime!''}" /></td>
        <td class="col-xs-1"><@text name="wayPoints[${idx}].distance" value="${rp.distance!''}" /></td>
    </tr>
    <tr>
        <#if !isReverseRoute && !viewMode>
            <#if add || remove>
            <td colspan="7" class="col-xs-12">
            </#if>
            <#if add>
                <a href="#" onclick="addRoutePoint(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
            </#if>
            <#if remove>
                <a href="#" onclick="removeRoutePoint(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
            </#if>
            <#if add || remove>
            </td>
            </#if>
        </#if>
    </tr>
</#macro>

<#macro btn formaction>
    <button type="button" formaction="${formaction}" onclick="submitForm(this)">
        <#nested/>
    </button>
</#macro>

<#macro hidden name value class="" id="">
    <@routedatafield id=id name=name value=value type="hidden" class=class />
</#macro>

<#macro text name value ro=false class="" id="">
    <@routedatafield id=id name=name value=value ro=ro class=class />
</#macro>

<#macro number name value ro=false class="" id="">
    <@routedatafield id=id name=name value=value type="number" ro=ro class=class />
</#macro>

<#macro date name value ro=false class="" id="">
    <@routedatafield id=id name=name value=value type="date" ro=ro class=class />
</#macro>

<#macro time name value ro=false class="" id="">
    <#assign iclass = ("time " + class)?trim />
    <@routedatafield id=id name=name value=value ro=ro class=iclass />
</#macro>

<#macro routedatafield id name value type="text" ro=false class="">
    <#assign iclass = ("route-data " + class)?trim />
    <#if viewMode>
        <#if type != "hidden">
            <@span id=id class=class>${value}</@>
        </#if>
    <#else>
        <@input id=id type=type name=name value=value ro=ro class=iclass />
    </#if>
</#macro>

<#macro input id name value class type="text" ro=false>
    <input
        <#if id?has_content>id="${id}"</#if>
        name="${name}"
        value="${value}"
        type="${type}"
        <#if class?has_content>class="${class}"</#if>
        <#if ro>readonly</#if>
    />

</#macro>
<#macro span id class>
    <span <#if id?has_content>id="${id}"</#if> <#if class?has_content>class="${class}"</#if>><#nested>&nbsp;</span>
</#macro>
