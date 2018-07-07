<#-- @ftlvariable name="countries" type="java.util.List<String>" -->
<#-- @ftlvariable name="route" type="info.getbus.servebus.route.model.Route" -->
<#-- @ftlvariable name="viewMode" type="boolean" -->

<#import "../../macro/forms.ftl" as form>

<@spring.bind "route" />

<#--<#assign readonlyMode = false />-->
<#assign isReverseRoute = route.direction == 'R' />
<#--<#assign restrictedit = route.direction == 'R' />-->
<#assign routeStopEditable = !viewMode && route.direction == 'F' />

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
        $('.time:not(#route-stop-template .time)').combodate(combodateConf);
    });

<#if !isReverseRoute && !viewMode>
    function addRouteStop(button) {
        let rowWithButton = $(button).parent().parent();
        let routeStopTemplate = $("#route-stop-template");
        rowWithButton.after(routeStopTemplate.find("tr:nth-child(2)").clone());
        let inputsRow = routeStopTemplate.find("tr:first-child").clone();
        inputsRow.attr("data-new-row-marker","this-is-new");
        rowWithButton.after(inputsRow);
        reindexRouteStops();
        inputsRow.find('.time').combodate(combodateConf);
        inputsRow = $("tr.route-stop[data-new-row-marker='this-is-new']");
        inputsRow.removeAttr("data-new-row-marker");
        let addressInput = inputsRow.find('input.address-autocomplete')[0];
        attachAutocomplete(addressInput/*, inputsRow.attr('data-stop-idx')*/);
        return false;
    }

    function removeRouteStop(button) {
        $(button).parent().parent().prev().remove();
        $(button).parent().parent().remove();
        reindexRouteStops();
        return false;
    }

    function reindexRouteStops() {
        $("form[name=route] tr.route-stop").each(function (idx) {
            let elem = $(this);
            if (elem.data("stop-idx") !== idx) {
                elem.attr("data-stop-idx", idx);
                elem.find(".route-data:input").each(function () {
                    let input = $(this);
                    let newName = input.attr("name").replace(/(stops\[)(-?\d+)(\])/, "$1" + idx + "$3");
                    input.attr("name", newName);
                })
            }
        });
    }

    function attachAutocomplete(elm) {
        $(elm).change(function () {
            fillInAddressFields(stopIdx(elm), {}); //clear
        });

        let autocomplete = new google.maps.places.Autocomplete(elm, autocompleteOpts);
        autocomplete.addListener('place_changed', function () {
            let place = autocomplete.getPlace();
            let address = new AddressAdapter(place);
            fillInAddressFields(stopIdx(elm), address);
        });
    }

    function initAutocomplete() {
        $('.address-autocomplete:not(#route-stop-template .address-autocomplete)').each(function (i, elm) {
            attachAutocomplete(elm);
        });
    }
    
    function stopIdx(someStopElem) {
        return $(someStopElem).closest('tr').attr("data-stop-idx");
    }

    class AddressAdapter {
        constructor(place) {
            let components = place.address_components;
            if (components) {
                components.forEach((elm) => {
                    let addressType = elm.types[0];
                    if (this[addressType]) {
                        this[addressType](elm);
                    }
                });
                this.street = [this.streetName, this.buildingNumber].join(" ").trim();
            }
            this.utcOffset = place.utc_offset;
            this.longitude = place.geometry.location.lng();
            this.latitude = place.geometry.location.lat();
        }
        street_number(addressComponent) { this.buildingNumber = addressComponent.long_name; }
        route(addressComponent) { this.streetName = addressComponent.long_name; }
        locality(addressComponent) { this.city = addressComponent.long_name; }
        administrative_area_level_1 (addressComponent) { this.adminArea1 = addressComponent.long_name; }
        country(addressComponent) { this.countryCode = addressComponent.short_name; }
        postal_code(addressComponent) { this.zip = addressComponent.short_name; }
    }

    function fillInAddressFields(idx, address) {
        qAddressField("Street").val(address.street);
        qAddressField("StreetName").val(address.streetName);
        qAddressField("BuildingNumber").val(address.buildingNumber);
        qAddressField("City").val(address.city);
        qAddressField("Zip").val(address.zip);
        qAddressField("AdminArea1").val(address.adminArea1);
        qAddressField("CountryCode").val(address.countryCode);
        qAddressField("UtcOffset").val(address.utcOffset);
        qStopFiald("lon").val(address.longitude);
        qStopFiald("lat").val(address.latitude);

        function qStopFiald(name) {
            return $(`:input[name='stops[${r"${idx}"}].${r"${name}"}']`);
        }
        function qAddressField(name) {
            return $(`:input[name='stops[${r"${idx}"}].address${r"${name}"}']`);
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
    <table style="display: none;" id="route-stop-template">
        <@routestop idx=-1 />
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
    <@text name="basePrice" value="${(route.basePrice?c)!''}" id="base-price"/>
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
                <th class="col-xs-2"><@spring.message "route.create.stop_name"/></th>
                <th class="col-xs-2"><@spring.message "route.create.country"/></th>
                <th class="col-xs-2"><@spring.message "route.create.address"/></th>
                <th class="col-xs-2"><@spring.message "route.create.depttime"/></th>
                <th class="col-xs-2"><@spring.message "route.create.arrtime"/></th>
                <th class="col-xs-1"><@spring.message "route.create.triptime"/></th>
                <th class="col-xs-1"><@spring.message "route.create.distance"/></th>
            </tr>
        </thead>
    <#list route.stops as stop>
        <@routestop idx=stop?index stop=stop
            add=!stop?is_last
            remove=!(stop?is_first||stop?is_last)
        />
    <#else>
        <@routestop idx="0" remove=false />
        <@routestop idx="1" add=false remove=false />
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

<#macro routestop idx stop={} add=true remove=true >
    <#assign stopAddress = {} />
    <#if stop.address??>
        <#assign stopAddress = stop.address />
    </#if>


    <tr class="route-stop" data-stop-idx="${idx}">
        <td class="col-xs-2">
            <#--TODO stop id? dont forget clear id when address edits-->
            <@text name="stops[${idx}].name" value=stop.name!"" ro=isReverseRoute <#--id="station"-->/>
            <@form.showFieldErrors 'stops[${idx}].name' 'error'/>
        </td>
        <#--<input name="stops[${idx}].addressId" value="${(stop.addressId?c)!''}" type="hidden" class="route-data"/> TODO clear address id when edit address -->
        <td class="col-xs-2">
                <#if viewMode>
                    <span><@spring.message 'country.'+stop.addressCountryCode /></span>
                <#elseif isReverseRoute>
                    <input name="stops[${idx}].addressCountryCode" value="${stop.addressCountryCode!''}" type="hidden" class="route-data"/>
                    <input value="<@spring.message 'country.'+stop.addressCountryCode />" readonly type="text"/>
                <#else>
                    <select name="stops[${idx}].addressCountryCode" class="route-data" id="country">
                        <#list countries as code>
                            <option ${(stop.addressCountryCode?? && code == stop.addressCountryCode)?then('selected','')} value="${code}"><@spring.message 'country.'+code /></option>
                        </#list>
                    </select>
                </#if>
        <#--<@form.showFieldErrors 'stops[${idx}].countryCode' 'error'/>-->
        </td>
        <td class="col-xs-2 address">
            <@hidden name="stops[${idx}].addressUtcOffset" value=stop.addressUtcOffset!"" />
            <@hidden name="stops[${idx}].long" value=stop.lon!"" />
            <@hidden name="stops[${idx}].lat" value=stop.lat!"" />
            <@hidden name="stops[${idx}].addressZip" value=stop.addressZip!"" />
            <@text name="stops[${idx}].addressAdminArea1" value=stop.addressAdminArea1!"" ro=true />
            <@text name="stops[${idx}].addressCity" value=stop.addressCity!"" ro=true />
            <@text id="stops[${idx}].addressStreet" name="stops[${idx}].addressStreet" value="${stop.addressStreet!''}" ro=isReverseRoute class="address-autocomplete" />
            <#--<@text name="stops[${idx}].address.streetBuilding" value="${stop.address.streetBuilding!''}" ro=isReverseRoute class="address-autocomplete" />-->
            <@hidden name="stops[${idx}].addressStreetName" value="${stop.addressStreetName!''}" />
            <@hidden name="stops[${idx}].addressBuildingNumber" value="${stop.addressBuildingNumber!''}" />
            <@form.showFieldErrors 'stops[${idx}].address*' 'error'/>
        </td>
        <td class="col-xs-2"><@time name="stops[${idx}].arrival" value="${stop.arrival!''}" /></td>
        <td class="col-xs-2"><@time name="stops[${idx}].departure" value="${stop.departure!''}" /></td>
        <td class="col-xs-1">
            <@text name="stops[${idx}].tripTime" value="${stop.tripTime!''}" />
            <@form.showFieldErrors 'stops[${idx}].tripTime' 'error'/>
        </td>
        <td class="col-xs-1">
            <@text name="stops[${idx}].distance" value="${stop.distance!''}"/>
            <@form.showFieldErrors 'stops[${idx}].distance' 'error'/>
        </td>
    </tr>
    <tr>
        <#if !isReverseRoute && !viewMode>
            <#if add || remove>
            <td colspan="7" class="col-xs-12">
            </#if>
            <#if add>
                <a href="#" onclick="addRouteStop(this)"><@spring.message "cabinet.partner.route.addStation"/></a>
            </#if>
            <#if remove>
                <a href="#" onclick="removeRouteStop(this)"><@spring.message "cabinet.partner.route.removeStation"/></a>
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
