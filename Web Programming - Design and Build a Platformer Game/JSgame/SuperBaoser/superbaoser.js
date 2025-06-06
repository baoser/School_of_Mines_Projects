function Platform(x,y,width,height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    this.draw = context => {
        context.fillStyle = 'red';
        context.fillRect(this.x,this.y,this.width,this.height);
    }
}

function Room(start,platforms){
    this.start = start;
    this.platforms = platforms;

    this.draw = context => {
        for(let platform of platforms){
            platform.draw(context);
        }
    }
}

const floors = [
    [
       new Room({x: 80,y:600},[
           new Platform(350,50,40,10),
           new Platform(210,150,40,10),
           new Platform(10,220,20,10),
           new Platform(180,270,100,10),
           new Platform(300,350,100,10),
           new Platform(200,420,100,10),
           new Platform(400,500,100,10),
           new Platform(50,650,100,10),
           new Platform(200,630,100,10),
           new Platform(500,600,100,10),
       ]),
        new Room({x: 500,y:650},[
            new Platform(450,675,100,10),
            new Platform(325,650,50,10),
    		new Platform(220,625,40,10),
    		new Platform(10,575,40,10),
    		new Platform(220,510,40,10),
    		new Platform(30,430,40,10),
    		new Platform(150,380,20,10),
    		new Platform(0,280,30,10),
    		new Platform(0,180,200,10),
    		new Platform(560,500,50,10),
    		new Platform(600,420,50,10),
    		new Platform(600,340,30,10),
    		new Platform(650,240,50,10),
    		new Platform(470,160,40,10),
    		new Platform(400,85,100,10),
        ])
    ],
    [
        new Room({x: 90,y: 310},[
            new Platform(70,340,40,10),
            new Platform(310,640,40,10),
            new Platform(510,590,40,10),
            new Platform(450,500,40,10),
            new Platform(520,460,40,10),
            new Platform(600,380,40,10),
            new Platform(440,310,40,10),
            new Platform(330,270,40,10),
            new Platform(150,180,40,10),
            new Platform(400,150,10,10),
            new Platform(480,70,10,10),
        ]),
        new Room({x: 600,y: 600},[
            new Platform(580,630,40,10),
            new Platform(400,560,40,10),
            new Platform(250,480,20,10),
            new Platform(180,390,20,10),
            new Platform(260,300,10,10),
            new Platform(380,210,10,10),
            new Platform(550,320,10,10),
            new Platform(650,230,10,10),
            new Platform(600,140,10,10),
            new Platform(600,50,10,10),
        ])
    ],
];

let scores = {
    fastest: {
        all: null,
        1: null,
        2: null
    },
    deaths: {
        all: null,
        1: null,
        2: null
    },
    last: {
        all: null,
        1: null,
        2: null
    }
}

function Player(){
    this.velocity = {x: 0,y: 0};
    this.speed = 4;
    this.jump = 11;
    this.friction = 0.8;

    this.controls = {
        left: false,
        right: false,
        up: false
    };

    this.position = null;
    this.floor = null;
    this.room = null;
    this.timer = null;
    this.deaths = 0;

    this.start = floor => {
        if(this.floor && this.floor !== floors[floor]){
            updateScore(floors.indexOf(this.floor) + 1,Math.floor((new Date().getTime() - this.timer.getTime()) / 1000),this.deaths);
        }
        this.floor = floors[floor];
        this.enter(this.floor[0]);
        this.timer = new Date();
        this.deaths = 0;
    };

    this.enter = (room,leave = true) => {
        this.position = {...room.start};
        this.velocity = {x: 0,y: 0.2};
        this.room = room;
    };

    this.tick = canvas => {
        // update x velocity
        let dx = this.controls.left ^ this.controls.right ? this.controls.left ? -1 : 1 : 0;
        this.velocity.x = (this.velocity.y === 0 ? this.friction : 1) * this.speed * dx;

        // update y velocity
        if(this.velocity.y < 0){ // jumping
            if(this.controls.up){
                this.velocity.y *= 0.9;
                if(this.velocity.y > -0.5)
                    this.velocity.y = 0.2;
            } else
                this.velocity.y = 0.2;
        } else if(this.findPlatform(0)){ // walking
            if(this.controls.up)
                this.velocity.y = -this.jump;
            else
                this.velocity.y = 0;
        } else // falling
            this.velocity.y += 0.2;

        // update x
        this.position.x += this.velocity.x;
        if(this.position.x < 10)
            this.position.x = 10;
        else if(this.position.x > canvas.width - 10)
            this.position.x = canvas.width - 10;

        // update y
        if(this.velocity.y > 0){
            let platform = this.findPlatform(Math.abs(this.velocity.y));
            if(platform)
                this.position.y = platform.y;
            else
                this.position.y += this.velocity.y;
        } else
            this.position.y += this.velocity.y;

        // test player position for death/advancement
        if(this.position.y > canvas.height){ // kill if out of bounds
            this.enter(this.room);
            this.deaths++;
        } else if(this.position.y < 0){
            let index = this.floor.indexOf(this.room);
            if(index === this.floor.length - 1){
                let floor = floors.indexOf(this.floor);
                if(floor === floors.length - 1)
                    alert('Congratulations, you beat the game!');
                else
                    alert('Congratulations, you beat the floor!');
                this.controls = {
                    left: false,
                    right: false,
                    up: false
                }
                this.start((floor + 1) % floors.length);
            } else
                this.enter(this.floor[index + 1])
        }
    };

    this.findPlatform = (steps) => {
        for(let platform of this.room.platforms) {
            let dy = this.position.y - platform.y;
            if(dy >= 0 && dy <= steps && (platform.x <= this.position.x + 10 && this.position.x - 10 <= platform.x + platform.width))
                return platform;
        }
        return null;
    };

    this.draw = context => {
        context.fillStyle = 'green';
        context.fillRect(this.position.x - 10,this.position.y - 20,20,20);
    }
}

let last = {
    time:{
        1: null,
        2: null,
    },
    deaths:{
        1: null,
        2: null
    }
}

let updateScore = (floor,time,deaths) => {
    let speed = scores['fastest']
    let death = scores['deaths'];
    if(time < (speed[floor] || Number.MAX_SAFE_INTEGER))
        speed[floor] = time;
    if(deaths < (death[floor] || Number.MAX_SAFE_INTEGER))
        death[floor] = deaths;
    last['time'][floor] = time;
    last['deaths'][floor] = deaths;
    scores['last'][floor] = time + 's, ' + deaths + ' deaths'
    if(floor === floors.length){
        let sumTime = 0,sumDeaths = 0;
        for(let [key,score] of Object.entries(last['time']))
            sumTime += score;
        for(let [key,score] of Object.entries(last['deaths']))
            sumDeaths += score;
        updateScore('all',sumTime,sumDeaths);
    }
}

let writeScore = (category,floor) => {
    let descriptor;
    switch(category){
        case 'fastest': descriptor = 's'; break;
        case 'deaths': descriptor = ' deaths'; break;
        default: descriptor = '';
    }
    $('#stats-' + category + '-' + floor).html(scores[category][floor] !== null ? (scores[category][floor] + descriptor) : 'n/a');
}

$(document).ready(() => {
    // detect which keys are pressed
    $(document).keypress(event => {
        switch(event.key){
            case 'w': player.controls.up = true;break;
            case 'a': player.controls.left = true;break;
            case 'd': player.controls.right = true;break;
        }
    });
    $(document).keyup(event => {
        switch(event.key){
            case 'w': player.controls.up = false;break;
            case 'a': player.controls.left = false;break;
            case 'd': player.controls.right = false;break;
        }
    });

    let player = new Player();

    let canvas = $('#game')[0];
    let context = canvas.getContext('2d');
    context.font = "50px Arial";
    context.fillStyle = "red";
    context.fillText("Press the Start Game Button", 40, 300);
    context.fillText("to begin playing", 180, 400);

    $(function () {
        $("#start").one("click", function(){
            $("#start").hide();
            player.start(0); // start floor
            let loop = setInterval(() => {
                let floor = player.floor;
                let room = player.room;

                // move player
                player.tick(canvas);

                // draw to canvas
                context.clearRect(0,0,canvas.width,canvas.height);
                room.draw(context);
                player.draw(context);

                // update stats
                for(const [category,value] of Object.entries(scores)){
                    for(const [type,score] of Object.entries(value)){
                        writeScore(category,type);
                    }
                }
            },17);
        });
    });
});