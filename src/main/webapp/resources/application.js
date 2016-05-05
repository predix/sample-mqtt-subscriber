var randomData;

$('#randomDataChart').highcharts({
  chart : {
    type : 'line',
    events : {
      load : function() {
        randomData = this.series[0];
      }
    }
  },
  title : {
    text : false
  },
  xAxis : {
    type : 'datetime',
    minRange : 60 * 1000
  },
  yAxis : {
    title : {
      text : false
    },
    min: -20,
    max: 20
  },
  legend : {
    enabled : false
  },
  plotOptions : {
    series : {
      threshold : 0,
      marker : {
        enabled : false
      }
    }
  },
  series : [ {
    name : 'Data',
      data : [ ]
    } ]
});

function setConnected(connected) {
  document.getElementById('connect').disabled = connected;
  document.getElementById('disconnect').disabled = !connected;
  document.getElementById('conversationDiv').style.visibility = connected ? 'visible'
      : 'hidden';
  document.getElementById('response').innerHTML = '';
}

function connect() {
  var socket = new SockJS('/accelerometer');
  socket.onopen = function() {
    console.log("Connected!")
  };
  socket.onmessage = function(message){
    showMessage(message);
  }
}

function showMessage(message) {
    var point = [ (new Date()).getTime(), parseInt(message.data) ];
    var shift = randomData.data.length > 60;
    randomData.addPoint(point, true, shift);
}
