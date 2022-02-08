function getWeather(latitu,longitu){
        $.ajax({
            type : "POST",
            url : "ajax",
            data : {
               latitude: latitu,
               longitude: longitu
            },
            success : function(response) {
                $('#weather_data').html('');
                $('#weather_data').append(response);
            },
            error : function(e) {
            alert(e);
            }
        });
}