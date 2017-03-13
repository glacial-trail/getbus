        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script src="/js/scripts/moment.js"></script>
        <script src="/js/scripts/combodate.js"></script>
        <script src="/js/3p/bootstrap.min.js"></script>
        <#--<script src="js/3p/locale.js"></script>-->

        <script>
            $(function(){
                $('.time').combodate({
                    firstItem: 'name', //show 'hour' and 'minute' string at first item of dropdown
                    minuteStep: 1
                });
            });
        </script>
    </body>
</html>
