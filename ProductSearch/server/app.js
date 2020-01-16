var createError = require('http-errors');
var express = require('express');
var path = require('path');
const http = require("http");
const https = require("https");
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var cors = require('cors');


var app = express();

// view engine setup
app.use(cors());
app.use(express.static("./public"));

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.get('/',(req, res)=>{
  res.send("Homework 9");
});


// autozip area
// ---------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------
app.get('/autozip/:tt', function(req, resq) {
  console.log(req.params.tt);
  url = "http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=" + encodeURIComponent(req.params.tt) + "&username=mrong&country=US&maxRows=5";
  // resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  http.get(url, res => {
    // res.setEncoding("utf8");

    res.on("data", data => {
      body += data;
    });
    res.on("end", () => {
      if (body.startsWith("[")) {
        body = body.substring(15);
      }
      body = JSON.parse(body);
      console.log(body);
      resq.json(body);
    });
  });
});


//search area
// ---------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------
app.get('/keyword/:keyword/category/:category/condition/:condition/shipoption/:shipoption/distance/:distance/zipcode/:zipcode', function(req, resq) {
  url="http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=MengjieR-homework-PRD-7a6d6c7c3-de7ecbd9&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=50&keywords="
      +encodeURIComponent(req.params.keyword)+
      "&buyerPostalCode="+req.params.zipcode;

  if(req.params.category!="-1"){
    url+="&categoryId="+req.params.category;
  }
  url+="&itemFilter(0).name=MaxDistance&itemFilter(0).value="+req.params.distance;

  if (req.params.shipoption.indexOf("FreeShipping")!=-1){
    url +="&itemFilter(1).name=FreeShippingOnly&itemFilter(1).value=true";
  }else{
    url +="&itemFilter(1).name=FreeShippingOnly&itemFilter(1).value=false";
  }

  if (req.params.shipoption.indexOf("LocalPickup")!=-1){
    url +="&itemFilter(2).name=LocalPickupOnly&itemFilter(2).value=true";
  }else{
    url +="&itemFilter(2).name=LocalPickupOnly&itemFilter(2).value=false";
  }

  url+="&itemFilter(3).name=HideDuplicateItems&itemFilter(3).value=true";

  if(req.params.condition.indexOf("-1")==-1){
    var conditionarray = req.params.condition.split(',');

    if(conditionarray.length>0){
      for (var i =0;i<conditionarray.length;i++){
        url+="&itemFilter(4).name=Condition&itemFilter(4).value("+i+")="+conditionarray[i];
      }
    }
  }
  url+="&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo";

  console.log(url);
  // resq.send(url);

  // resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  http.get(url, res => {
    // res.setEncoding("utf8");
    res.on("data", data => {
      body += data;
    });
    res.on("end", () => {
      body = JSON.parse(body);
      resq.json(body);
    });
  });
});


app.get('/keyword/:keyword/category/:category/condition/:condition/shipoption/:shipoption', function(req, resq) {
  url="http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=MengjieR-homework-PRD-7a6d6c7c3-de7ecbd9&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=50&keywords="
      +encodeURIComponent(req.params.keyword);

  if(req.params.category!="-1"){
    url+="&categoryId="+req.params.category;
  }

  if (req.params.shipoption.indexOf("FreeShipping")!=-1){
    url +="&itemFilter(0).name=FreeShippingOnly&itemFilter(0).value=true";
  }else{
    url +="&itemFilter(0).name=FreeShippingOnly&itemFilter(0).value=false";
  }

  if (req.params.shipoption.indexOf("LocalPickup")!=-1){
    url +="&itemFilter(1).name=LocalPickupOnly&itemFilter(1).value=true";
  }else{
    url +="&itemFilter(1).name=LocalPickupOnly&itemFilter(1).value=false";
  }

  url+="&itemFilter(2).name=HideDuplicateItems&itemFilter(2).value=true";

  if(req.params.condition.indexOf("-1")==-1){
    var conditionarray = req.params.condition.split(',');

    if(conditionarray.length>0){
      for (var i =0;i<conditionarray.length;i++){
        url+="&itemFilter(3).name=Condition&itemFilter(3).value("+i+")="+conditionarray[i];
      }
    }
  }
  url+="&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo";

  console.log(url);
  // resq.send(url);

  // resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  http.get(url, res => {
    // res.setEncoding("utf8");
    res.on("data", data => {
      body += data;
    });
    res.on("end", () => {
      body = JSON.parse(body);
      resq.json(body);
    });
  });
});











app.get('/itemspec/:itemid', function(req, resq) {
  url ="http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=MengjieR-homework-PRD-7a6d6c7c3-de7ecbd9&siteid=0&version=967&ItemID="
      +req.params.itemid+"&IncludeSelector=Description,Details,ItemSpecifics";
  console.log(url);
  // resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  http.get(url, res => {
    // res.setEncoding("utf8");
    res.on("data", data => {
      body += data;
    });
    res.on("end", () => {
      if (body.startsWith("[")){
        body = body.substring(15);
      }
      body = JSON.parse(body);
      resq.json(body);
    });
  });
});

app.get('/phototitle/:itemtitle', function(req, resq) {
  url ="https://www.googleapis.com/customsearch/v1?q="
      +encodeURIComponent(req.params.itemtitle)
      +"&cx=011245205246728870522:trecr0sit54&imgSize=huge&imgType=news&num=8&searchType=image&key=AIzaSyCKkadzs925rUqvdCMKrF_fgfDxhvbKfJ8";

  console.log(url);
  // resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  https.get(url, res => {
    // res.setEncoding("utf8");
    res.on("data", data => {
      body += data;
    });
    res.on("end", () => {
      if (body.startsWith("[")){
        body = body.substring(15);
      }
      body = JSON.parse(body);
      resq.json(body);
    });

  });
});


app.get('/similarid/:itemid', function(req, resq) {
  url ="http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=MengjieR-homework-PRD-7a6d6c7c3-de7ecbd9&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId="
      +encodeURIComponent(req.params.itemid)+"&maxResults=20";
  console.log(url);
  // resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  http.get(url, res => {
    // res.setEncoding("utf8");
    res.on("data", data => {
      body += data;
    });
    res.on("end", () => {
      if (body.startsWith("[")){
        body = body.substring(15);
      }
      body = JSON.parse(body);
      resq.json(body);
    });
  });
});







// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});


module.exports = app;
app.listen(process.env.port || 8081, () =>
    console.log('Example app listening on port 8081!'));
