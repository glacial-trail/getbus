<div>
    hello username

    you timezone is

    <select name="timezone" id="my-timezone">
    <#list zones as zone>
        <option ${(profile.timezone?? == zone)?then('selected','')} value="${zone}">
            <#--<@spring.message 'country.'+code />-->
        </option>
    </#list>
    </select>
</div>