package com.example.weatherforecast.model.Pojos

data class Settings(var lang:String=Constants.LANG_EN,var isMap:Boolean=false,var unit:String=Constants.UNITS_DEFAULT,
                    var lat:Double=0.0,var lon:Double=0.0)
