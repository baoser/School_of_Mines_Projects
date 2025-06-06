var mongoose = require('mongoose');
require('ME');

var assignmentschema = new mongoose.Schema({
    points:{
        type: Number,
        required: true,
        "default": 0,
        min:0,
        max:100
    },
    due: String,
    name:String
});

var courseschema = new mongoose.Schema({
    ID: Number,
    abbreviation: String,
    name: String,
    assignments: [assignmentschema]
});

function INSECONDFILE(){
}


mongoose.model('courses', courseschema);