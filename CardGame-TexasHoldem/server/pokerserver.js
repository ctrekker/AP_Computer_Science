const http=require('http');
const crypto=require('crypto');
const url=require('url');

var servers={};
var allPlayers={};

var serverCount=0;

newServer('Main Server');
// var p=newPlayer('Ctrekker');
// servers[Object.keys(servers)[0]].players.push(p.uuid);
// servers[Object.keys(servers)[0]].gameStarted=true;
// var p2=newPlayer('Ctrekker2');
// servers[Object.keys(servers)[0]].players.push(p2.uuid);
// servers[Object.keys(servers)[0]].gameStarted=true;
const server=http.createServer(function(req, res) {
    var data;
    var sUrl=url.parse(req.url, true);
    var command=sUrl.pathname.replace('/', '');
    var q = sUrl.query;
    switch(command) {
        case '':
            res.write(JSON.stringify(servers));
            res.write('\n\n'+JSON.stringify(allPlayers));
            break;
        case 'listServers':
            var metadata={};
            const serversKeys=Object.keys(servers);
            for(var i=0; i<serversKeys.length; i++) {
                const server = servers[serversKeys[i]];
                metadata[server.name] = {
                    uuid: server.uuid,
                    started: server.gameStarted,
                    players: server.players,
                    name: server.name
                };
            }
            data={
                list: Object.keys(servers).join(','),
                meta: metadata,
                length: servers.length
            };
            res.write(JSON.stringify(data));
            break;
        case 'serverInit':
            serverCount++;
            if(q.name!==undefined) {
                newServer(q.name);
                res.write(JSON.stringify(servers));
            }
            else {
                newServer("Server #"+serverCount);
                res.write(JSON.stringify(servers));
            }
            break;
            /*
            name: name,
        uuid: bytesToHex(crypto.randomBytes(8)),
        players: [],
        gameStarted: false,
        data: {
            /*

            phase: 0,
                deck: new CardDeck(),
            dealerIndex: 0,
            betterIndex: 1,
            lbIndex: null,
            bbIndex: null,
            drawn: [],
            pot: new ChipBank(),
            roundCount: 0,
            minimumBet: 2
             */
        case 'serverInfo':
            if(q.uuid!==undefined) {
                if(servers[q.uuid]!==undefined) {
                    const server=servers[q.uuid];
                    res.write(JSON.stringify(server));
                }
                else {
                    res.write(JSON.stringify({
                        'error': 'uuid does not correspond to server'
                    }));
                }
            }
            else {
                res.write(JSON.stringify({
                    'error': 'specify a server uuid'
                }));
            }
            break;
        case 'clientInit':
            if(q.username!==undefined) {
                var player=newPlayer(q.username);
                res.write(JSON.stringify(player));
            }
            else {
                res.write(JSON.stringify({
                    'error': 'provide a username'
                }));
            }
            break;
        case 'clientInfo':
            if(q.uuid!==undefined) {
                if(allPlayers[q.uuid]!==undefined) {
                    var currentPlayer=allPlayers[q.uuid];
                    var obj={
                        username: currentPlayer.obj.username,
                        uuid: currentPlayer.uuid,
                        dealer: currentPlayer.obj.dealer,
                        chips: currentPlayer.obj.chips.toJson(),
                        bet: currentPlayer.obj.bet,
                        totalBet: currentPlayer.obj.totalBet,
                        betRound: currentPlayer.obj.betRound,
                        hasBet: currentPlayer.obj.hasBet,
                        betting: currentPlayer.obj.betting,
                        folded: currentPlayer.obj.folded,
                        blind: currentPlayer.obj.blind,
                        hasLost: currentPlayer.obj.hasLost
                    };
                    // Show cards if same player, or if we're on phase 10
                    try {
                        if ((q.key !== undefined && allPlayers[q.uuid].key === q.key) || getPlayerServer(currentPlayer).data.revealed) {
                            obj.hand = allPlayers[q.uuid].obj.hand;
                        }
                        res.write(JSON.stringify(obj));
                    }
                    catch(error) {
                        res.write(JSON.stringify(obj));
                    }
                }
                else {
                    res.write(JSON.stringify({
                        'error': 'player with specified uuid does not exist',
                        'code': 1404
                    }))
                }
            }
            else {
                res.write(JSON.stringify({
                    'error': 'provide a valid uuid'
                }))
            }

            break;
        case 'serverJoin':
            if(q.key!==undefined&&q.uuid!==undefined) {
                const key=q.key;
                const uuid=q.uuid;

                if(allPlayers[uuid]===undefined) {
                    res.write(JSON.stringify({
                        'error': 'BAD KEY'
                    }));
                }
                // Success
                else if(allPlayers[uuid].uuid===uuid&&allPlayers[uuid].key===key) {
                    var player=allPlayers[uuid];
                    var serverUuid=Object.keys(servers)[0];
                    if(q.sid!==undefined) {
                        serverUuid=q.sid;
                    }
                    const server=servers[serverUuid];
                    server.players.push(player.uuid);
                    res.write(JSON.stringify(server));
                }
                else {
                    res.write(JSON.stringify({
                        'error': 'BAD UUID'
                    }));
                }
            }
            else {
                res.write(JSON.stringify({
                    'error': 'include the desired user\'s key and uuid'
                }));
            }
            break;
        case 'startServer':
            if(q.uuid!==undefined) {
                if(servers[q.uuid]!==undefined) {
                    if(servers[q.uuid].players.length>=2) {
                        serverCount++;
                        newServer("Server #" + serverCount);
                        servers[q.uuid].gameStarted = true;
                    }
                    else {
                        res.write(JSON.stringify({
                            'error': 'too few players!'
                        }));
                    }
                }
                else {
                    res.write(JSON.stringify({
                        'error': 'server with specified uuid doesn\'t exist'
                    }));
                }
            }
            else {
                res.write(JSON.stringify({
                    'error': 'include server uuid'
                }));
            }
            break;
        case "action_setBet":
            if(validUser(q, res)&&q.bet!==undefined) {
                var player=allPlayers[q.uuid];
                var server=getPlayerServer(player);
                if(!(parseInt(q.bet)<server.data.minimumBet)) {
                    player.obj.hasBet = true;
                    player.obj.bet = server.data.currentBet + parseInt(q.bet);

                    // Put in max bet if player bets greater than total
                    if (player.obj.bet > player.obj.chips.getTotal()) {
                        player.obj.bet = player.obj.chips.getTotal();
                    }
                }
                else {
                    res.write(JSON.stringify({
                        'error': 'too_low'
                    }));
                }
            }
            break;
        case "action_setFolded":
            console.log("Player "+q.uuid+" attempted to folded");
            if(validUser(q, res)&&q.folded!==undefined) {
                console.log("Player "+q.uuid+" folded");
                allPlayers[q.uuid].obj.folded=q.folded==="true";
            }
            break;
        case "action_call":
            if(validUser(q, res)) {
                var player=allPlayers[q.uuid];
                var server=getPlayerServer(player);
                player.obj.bet=server.data.currentBet-player.obj.betRound;

                // Put in max bet if player bets greater than total
                if(player.obj.bet>player.obj.chips.getTotal()) {
                    player.obj.bet=player.obj.chips.getTotal();
                }
                player.obj.hasBet=true;
                allPlayers[q.uuid]=player;
            }
            break;
    }
    res.end();
});
function getPlayerServer(player) {
    const serversKeys=Object.keys(servers);
    for(var i=0; i<serversKeys.length; i++) {
        const server=servers[serversKeys[i]];
        for(var u=0; u<server.players.length; u++) {
            if(server.players[u]===player.uuid) return server;
        }
    }
    return null;
}
function validUser(q, res) {
    if(q.uuid!==undefined&&q.key!==undefined) {
        if(allPlayers[q.uuid]!==undefined) {
            if(allPlayers[q.uuid].key===q.key) {
                return true
            }
            else {
                res.write(JSON.stringify({
                    'error': 'invalid key'
                }));
            }
        }
        else {
            res.write(JSON.stringify({
                'error': 'player with specified uuid doesn\'t exist'
            }));
        }
    }
    else {
        res.write(JSON.stringify({
            'error': 'bet, key, and uuid are needed'
        }));
    }
    return false;
}
function newServer(name) {
    const template={
        name: name,
        uuid: bytesToHex(crypto.randomBytes(8)),
        players: [],
        gameStarted: false,
        data: {
            /*
            PHASE INDEX:
            0  -> Init
            1  -> Blinds
            2  -> Deal
            3  -> Second bet (before flop)
            4  -> Flop reveal
            5  -> Third bet (before turn)
            6  -> Turn reveal
            7  -> Fourth bet (before river)
            8  -> River reveal
            9  -> Fifth bet (final)
            10 -> Card reveal
            11 -> Reset + dealer/blind changes
             */
            phase: 0,
            deck: new CardDeck(),
            dealerIndex: 0,
            betterIndex: 1,
            lbIndex: null,
            bbIndex: null,
            drawn: [],
            pot: new ChipBank(),
            roundCount: 1,
            minimumBet: 2,
            currentBet: 0,
            gameOver: false,
            revealCount: 0,
            revealed: false
        }
    };
    servers[template.uuid]=template;
}
function newPlayer(username) {
    var uuid=bytesToHex(crypto.randomBytes(16));
    var data = {
        uuid: uuid,
        key: bytesToHex(crypto.randomBytes(8)),
        obj: new Player(username, uuid)
    };
    allPlayers[data.uuid]=data;
    return data;
}
function bytesToHex(bytes) {
    return new Buffer(bytes).toString('hex');
}
server.listen(52);

setInterval(function() {
    const serversKeys=Object.keys(servers);
    for(var i=0; i<serversKeys.length; i++) {
        const server=servers[serversKeys[i]];
        // Handle each server's calculations
        // if(server.players.length>=2&&!server.gameStarted) setTimeout(function() {
        //     server.gameStarted=true;
        //     serverCount++;
        //     newServer("Server #"+serverCount);
        // }, 2000);
        runServerTick(servers[serversKeys[i]]);
    }
}, 100);
var count=0;
function runServerTick(server) {
    var players=getPlayersInServer(server);
    if(server.gameStarted&&!server.data.gameOver) {
        switch (server.data.phase) {
            // Init
            case 0:
                if (!players[server.data.dealerIndex].obj.isDealer()) {
                    for (var i = 0; i < players.length; i++) {
                        players[i].obj.isDealer(false);
                        players[i].obj.setBlind(0);
                        players[i].obj.betting=false;
                    }

                    players[server.data.dealerIndex].obj.setDealer(true);

                    var lbIndex = server.data.dealerIndex + 1;

                    var bbIndex = lbIndex + 1;
                    while (lbIndex >= players.length) {
                        lbIndex -= players.length;
                    }
                    while (bbIndex >= players.length) {
                        bbIndex -= players.length;
                    }
                    server.data.betterIndex=lbIndex;
                    players[server.data.betterIndex].obj.betting=true;

                    players[lbIndex].obj.setBlind(1);
                    players[bbIndex].obj.setBlind(2);
                    server.data.lbIndex=lbIndex;
                    server.data.bbIndex=bbIndex;
                }
                server.data.phase++;
                break;
            // Blinds
            case 1:
                for(var i=0; i<players.length; i++) {
                    var player=players[i];
                    if(player.obj.blind!==0) {
                        // Little blind
                        if(player.obj.blind===1) {
                            player.obj.totalBet=server.data.minimumBet/2;
                            player.obj.betRound=server.data.minimumBet/2;
                            player.obj.chips.transferTo(server.data.pot, server.data.minimumBet/2);
                        }
                        // Big blind
                        else if(player.obj.blind===2) {
                            player.obj.totalBet=server.data.minimumBet;
                            player.obj.betRound=server.data.minimumBet;
                            player.obj.chips.transferTo(server.data.pot, server.data.minimumBet);
                        }
                    }
                }
                server.data.phase++;
                break;
            // Deal
            case 2:
                for(var i=0; i<players.length; i++) {
                    const card1=server.data.deck.drawCard();
                    const card2=server.data.deck.drawCard();
                    players[i].obj.hand=[card1, card2];
                }
                server.data.currentBet=server.data.minimumBet;
                server.data.phase++;
                break;
            // Flop Bet
            case 3:
                doBettingTransfers();
                if(runBettingTick()) {
                    server.data.phase++;
                }
                // for(var i=0; i<players.length; i++) {
                //     if(players[i].obj.betting) {
                //         players[i].obj.bet = 2;
                //         players[i].obj.hasBet = true;
                //     }
                // }
                break;
            // Flop Reveal
            case 4:
                for(var i=0; i<3; i++) {
                    server.data.drawn[i]=server.data.deck.drawCard();
                }
                server.data.phase++;
                break;
            // Turn Bet
            case 5:
                doBettingTransfers();
                if(runBettingTick()) {
                    server.data.phase++;
                }
                break;
            // Turn Reveal
            case 6:
                server.data.drawn[3]=server.data.deck.drawCard();
                server.data.phase++;
                break;
            // River Bet
            case 7:
                doBettingTransfers();
                if(runBettingTick()) {
                    server.data.phase++;
                }
                break;
            // River Reveal
            case 8:
                server.data.drawn[4]=server.data.deck.drawCard();
                server.data.phase++;
                break;
            // Final Bet
            case 9:
                doBettingTransfers();
                if(runBettingTick()) {
                    //server.data.drawn=[new Card(0, 10), new Card(1, 10), new Card(0, 11), new Card(1, 11), new Card(2, 11)];
                    server.data.phase++;
                }
                break;
            // Card reveal and scoring
            case 10:
                // Make cards available to see
                if(server.data.revealCount===0) {
                    server.data.revealed=true;
                }
                server.data.revealCount++;
                if(server.data.revealCount>10*10) {
                    server.data.phase++;
                    server.data.revealCount=0;
                }
                break;
            // Reset + dealer/blind changes
            case 11:
                // Detect folds
                var numFolds=0;
                var notFolded=null;
                var nfIndex=0;
                for(var i=0; i<players.length; i++) {
                    if(players[i].obj.folded) numFolds++;
                    if(!players[i].obj.folded) {
                        notFolded=players[i];
                        nfIndex=i;
                    }
                }
                if(numFolds+1>=players.length) {
                    if(notFolded===null) break;
                    server.data.pot.transferTo(notFolded.obj.chips, server.data.pot.getTotal());
                    players[nfIndex]=notFolded;
                    server.data.roundCount++;
                    reset();
                    break;
                }

                // Detect battle
                var winnerList=[];
                var playerHighest=null;
                var scoreHighest=0;
                var specificHighest=0;
                for(var i=0; i<players.length; i++) {
                    var currentPlayer=players[i];
                    console.log(currentPlayer.uuid);
                    const score=currentPlayer.obj.calculateHandScore();
                    if(score>scoreHighest) {
                        scoreHighest=score;
                        specificHighest=currentPlayer.obj.score;
                        playerHighest=currentPlayer;
                        winnerList=[];
                    }
                    else if(score===scoreHighest) {
                        if(currentPlayer.obj.score>specificHighest) {
                            scoreHighest=score;
                            specificHighest=currentPlayer.obj.score;
                            playerHighest=currentPlayer;
                            winnerList=[];
                        }
                        else if(currentPlayer.obj.score===specificHighest) {
                            // Its a tie game
                            winnerList.push(currentPlayer);
                            winnerList.push(playerHighest);
                        }
                    }
                }
                if(playerHighest===null) console.log("This should never happen");
                else if(winnerList.length>0) {
                    for(var i=0; i<winnerList.length; i++) {
                        server.data.pot.transferTo(winnerList[i].obj.chips, server.data.pot.getTotal()/winnerList.length);
                        server.data.roundCount++;
                        reset();
                    }
                }
                else {
                    server.data.pot.transferTo(playerHighest.obj.chips, server.data.pot.getTotal());
                    server.data.roundCount++;
                    reset();
                }
                break;
        }
    }
    function reset() {
        // This will ripple to change the blinds in phase 0 (init)
        server.data.dealerIndex++;
        if(server.data.dealerIndex>=players.length) {
            server.data.dealerIndex=0;
        }

        server.data.deck=new CardDeck();
        server.data.drawn=[];
        server.data.currentBet=0;
        server.data.revealCount=0;
        server.data.revealed=false;

        // Reset player-specific attributes
        for(var i=0; i<players.length; i++) {
            var player=players[i];
            player.obj.hasBet=false;
            player.obj.betting=false;
            player.obj.bet=null;
            player.obj.totalBet=0;
            player.obj.betRound=0;
            player.obj.folded=false;
            player.obj.hand=[];
            player.obj.dealer=false;

            // Check for losers
            if(player.obj.chips.getTotal()<=0) {
                console.log("Player "+player.uuid+" has lost!");
                player.obj.hasLost=true;
                server.players.splice(server.players.indexOf(player.uuid), 1);

                if(server.players.length===1) {
                    server.data.gameOver=true;
                }
            }
        }

        server.data.phase=0;
    }
    function doBettingTransfers() {
        for(var i=0; i<players.length; i++) {
            if(players[i].obj.hasBet&&players[i].obj.bet!==null) {
                // Make sure the player's bet is more than the minimum
                // UNLESS its equal to the current bet
                if(players[i].obj.bet<server.data.minimumBet&&players[i].obj.bet!==server.data.currentBet) {
                    if(server.data.phase!==3) {
                        players[i].obj.bet=server.data.minimumBet;
                    }
                }
                // Check if the bet is higher than the server's highest bet
                if(players[i].obj.bet>server.data.currentBet) {
                    server.data.currentBet=players[i].obj.bet;
                }
                // If it's less or equal, set it to the server's bet, unless all in
                else if(!players[i].obj.chips.getTotal()<=0&&players[i].obj.bet+players[i].obj.betRound<server.data.currentBet) {
                    if(server.data.phase!==3) {
                        players[i].obj.bet=server.data.currentBet;
                    }
                }

                // Put in max bet if player bets greater than total
                if(players[i].obj.bet>players[i].obj.chips.getTotal()) {
                    players[i].obj.bet=players[i].obj.chips.getTotal();
                }

                players[i].obj.chips.transferTo(server.data.pot, players[i].obj.bet);
                players[i].obj.hasBet=true;
                players[i].obj.betting=false;
                players[i].obj.betRound+=players[i].obj.bet;
                players[i].obj.totalBet+=players[i].obj.bet;
                players[i].obj.bet=null;
            }
        }
    }
    function runBettingTick() {
        var bettingDone=true;
        var numFolded=0;
        for(var i=0; i<players.length; i++) {
            players[i].obj.betting=false;
            if(players[i].obj.folded) {
                numFolded++;
                continue;
            }

            if((!players[i].obj.hasBet||(players[i].obj.betRound<server.data.currentBet&&!players[i].obj.folded))&&!(players[i].obj.chips.getTotal()===0&&!players[i].obj.folded)) {
                bettingDone=false;
            }
        }
        // If there's only one player still in, then the round is over
        if(numFolded+1>=players.length) {
            server.data.phase=11;
            return false;
        }
        players[server.data.betterIndex].obj.betting=true;
        for(var i=0; i<players.length; i++) {
            // If the player is supposed to bet, but has already folded, then
            if(players[i].obj.betting&&players[i].obj.folded) {
                // Increment the current better and set it to that player
                players[i].obj.betting=false;
                incrementBetter();
            }
            // If the player is betting, and the client has sent a bet, then
            else if(players[i].obj.betting&&players[i].obj.hasBet&&!players[i].obj.folded) {
                incrementBetter();
            }
        }
        // If the betting is done reset the betting variables
        if(bettingDone) {
            server.data.currentBet=0;
            for(var i=0; i<players.length; i++) {
                players[i].obj.hasBet=false;
                players[i].obj.bet=null;
                players[i].obj.betRound=0;
                server.data.betterIndex=server.data.lbIndex;
            }
        }
        return bettingDone;
    }
    function incrementBetter() {
        server.data.betterIndex++;
        var first=true;
        while(server.data.betterIndex>=players.length) {
            server.data.betterIndex-=players.length;
            if(first) {
                first=false;
                for(var i=0; i<players.length; i++) {
                    if(players[i].obj.betRound>=server.data.currentBet) {
                        // The player is good
                        // players[i].obj.hasBet=true;
                    }
                    else {
                        players[i].obj.hasBet=false;
                    }
                }
            }
        }
        players[server.data.betterIndex].obj.betting=true;
    }
}
function getPlayersInServer(server) {
    var out=[];

    for(var i=0; i<server.players.length; i++) {
        out.push(allPlayers[server.players[i]]);
    }

    return out;
}

/*
Creates a new card
 */
function Card(type, value) {
    /*
    0=Diamonds
    1=Hearts
    2=Spades
    3=Clubs
     */
    if(type>=0&&type<=3) {
        this.type=type;
    }
    else {
        this.type=0;
    }
    /*
    1=Ace
    11=Jack
    12=Queen
    13=King
     */
    if(value>=1&&value<=13) {
        this.value=value;
    }
    else {
        this.value=2;
    }

    this.equals=function(other) {
        return this.type===other.type&&this.value===other.value;
    };
}
function randomCard() {
    return new Card(
        Math.floor(Math.random()*4),
        Math.floor(Math.random()*13)+1
    );
}
/*
Creates a new randomized deck of cards
 */
function CardDeck() {
    // Checks if a certain card is in the deck
    this.hasCard=function(other) {
        for(var i=0; i<this.data.length; i++) {
            if(this.data[i].equals(other)) {
                return true;
            }
        }
        return false;
    };
    this.drawCard=function() {
        var card=this.data[0];
        this.data.splice(0, 1);
        return card;
    };


    this.data=[];
    for(var i=0; i<52; i++) {
        var card=randomCard();
        while(this.hasCard(card)) {
            card=randomCard();
        }
        this.data.push(card);
    }
}


function ChipBank(c100, c50, c25, c5, c1) {
    this.c1=c1||0;
    this.c5=c5||0;
    this.c25=c25||0;
    this.c50=c50||0;
    this.c100=c100||0;
    this.total=0;

    this.calculateTotal=function() {
        this.total=this.c1+this.c5*5+this.c25*25+this.c50*50+this.c100*100;
    };
    this.getTotal=function() {
        this.calculateTotal();
        return this.total;
    };
    this.reset=function() {
        this.c1=0;
        this.c5=0;
        this.c25=0;
        this.c50=0;
        this.c100=0;
    };
    this.simplify=function() {
        const t100=Math.floor(this.getTotal()/100);
        const r100=this.getTotal()%100;
        const t50=Math.floor(r100/50);
        const r50=r100%50;
        const t25=Math.floor(r50/25);
        const r25=r50%25;
        const t5=Math.floor(r25/5);
        const r5=r25%5;
        const t1=Math.floor(r5/1);

        this.c1=t1;
        this.c5=t5;
        this.c25=t25;
        this.c50=t50;
        this.c100=t100;

        this.calculateTotal();
    };
    this.add=function(amount) {
        const preTotal=this.getTotal();
        this.reset();
        this.c1=preTotal+amount;
        this.simplify();
    };
    this.subtract=function(amount) {
        const preTotal=this.getTotal();
        this.reset();
        this.c1=preTotal-amount;
        this.simplify();
    };
    this.transferTo=function(other, amount) {
        other.add(amount);
        this.subtract(amount);
    };
    this.toJson=function() {
        return {
            c1: this.c1,
            c5: this.c5,
            c25: this.c25,
            c50: this.c50,
            c100: this.c100,
            total: this.calculateTotal()
        };
    }
}


function Player(username, uuid) {
    this.uuid=uuid;
    this.username=username;
    this.hand=[];
    this.dealer=false;
    this.chips=new ChipBank();
    this.chips.add(250);
    this.bet=null;
    this.betRound=0;
    this.totalBet=0;
    this.betting=false;
    this.hasBet=false;
    this.folded=false;
    this.hasLost=false;
    this.score=0;
    /*
    0 -> No blind
    1 -> Little blind
    2 -> Big blind
     */
    this.blind=0;

    this.isDealer=function() {
        return this.dealer;
    };
    this.isNoBlind=function() {
        return this.blind===0;
    };
    this.isLittleBlind=function() {
        return this.blind===1;
    };
    this.isBigBlind=function() {
        return this.blind===2;
    };
    this.setDealer=function(dealer) {
        this.dealer=dealer;
    };
    this.setBlind=function(blind) {
        this.blind=blind;
    };
    this.getFullHand=function() {
        var server=getPlayerServer(allPlayers[this.uuid]);
        var out=[];
        for(var i=0; i<this.hand.length; i++) {
            out.push(this.hand[i]);
        }
        for(var i=0; i<server.data.drawn.length; i++) {
            out.push(server.data.drawn[i]);
        }
        return out;
    };
    this.hasHighCard=function() {
        this.score=0;
        var highestCard=null;
        var fullHand=this.getFullHand();
        for(var i=0; i<fullHand.length; i++) {
            if(highestCard===null) highestCard=fullHand[i];
            else {
                if(fullHand[i].value>highestCard.value) {
                    highestCard.value=fullHand[i].value;
                }
            }
        }
        this.score=highestCard.value;
        return true;
    };
    this.hasPair=function() {
        this.score=0;
        var fullHand=this.getFullHand();
        var pairCount=0;
        for(var x=0; x<fullHand.length; x++) {
            for(var y=0; y<fullHand.length; y++) {
                if(x===y) continue;
                if(fullHand[x].value===fullHand[y].value) {
                    this.score+=fullHand[y].value;
                    pairCount++;
                }
            }
        }
        return pairCount>0;
    };
    this.hasTwoPair=function() {
        this.score=0;
        var fullHand=this.getFullHand();
        var pairCount=0;
        var lpi=-1;
        for(var x=0; x<fullHand.length; x++) {
            for(var y=0; y<fullHand.length; y++) {
                if(x===y||x===lpi||y===lpi) continue;
                if(fullHand[x].value===fullHand[y].value) {
                    this.score+=fullHand[x].value;
                    pairCount++;
                    lpi=x;
                }
            }
        }
        return pairCount>1;
    };
    this.hasThreeKind=function() {
        this.score=0;
        var fullHand=this.getFullHand();
        var threeCount=0;
        for(var x=0; x<fullHand.length; x++) {
            for(var y=0; y<fullHand.length; y++) {
                for(var z=0; z<fullHand.length; z++) {
                    if (x === y || x === z || y === z) continue;
                    if (fullHand[x].value === fullHand[y].value && fullHand[x].value === fullHand[z].value) {
                        this.score+=fullHand[x].value;
                        threeCount++;
                    }
                }
            }
        }
        return threeCount>0;
    };
    this.hasStraight=function() {
        this.score=0;
        var fullHand=this.getFullHand();
        for(var i=0; i<fullHand.length; i++) {
            if(
                hasCardWithValue(fullHand, fullHand[i].value+1)&&
                hasCardWithValue(fullHand, fullHand[i].value+2)&&
                hasCardWithValue(fullHand, fullHand[i].value+3)&&
                hasCardWithValue(fullHand, fullHand[i].value+4)
            ) {
                this.score=fullHand[i].value;
                return true;
            }
        }
        return false;
    };
    this.hasFlush=function() {
        this.score=0;
        var posibleScore=0;
        var fullHand=this.getFullHand();
        var desiredType=-1;
        var mostOfType=-1;
        for(var i=0; i<4; i++) {
            var currentCount=0;
            for(var x=0; x<fullHand.length; x++) {
                if(fullHand[x].type===i) currentCount++;
            }
            if(currentCount>mostOfType) {
                mostOfType=currentCount;
                desiredType=i;
            }
        }
        var typeCount=0;
        for(var i=0; i<fullHand.length; i++) {
            if(fullHand[i].type===desiredType) {
                posibleScore+=fullHand[i].value;
                typeCount++;
            }
        }
        if(typeCount>=5) this.score=posibleScore;
        return typeCount>=5;
    };
    this.hasFullHouse=function() {
        this.score=0;
        var possibleScore=0;
        if(!this.hasThreeKind()) return false;
        var fullHand=this.getFullHand();
        var pairNumber=-1;
        var threeNumber=-1;
        // Check for three of a kind
        for(var x=0; x<fullHand.length; x++) {
            var currentCount=0;
            for(var y=0; y<fullHand.length; y++) {
                if(fullHand[x].value===fullHand[y].value) currentCount++;
            }
            if(currentCount>=3) {
                possibleScore+=fullHand[x].value;
                threeNumber=fullHand[x].value;
                break;
            }
        }
        // Check for two of a kind
        for(x=0; x<fullHand.length; x++) {
            currentCount=0;
            for(y=0; y<fullHand.length; y++) {
                if(fullHand[x].value===fullHand[y].value) currentCount++;
            }
            if(currentCount>=2&&fullHand[x].value!==threeNumber) {
                possibleScore+=fullHand[x].value;
                pairNumber=fullHand[x].value;
                break;
            }
        }
        const condition=pairNumber>-1&&threeNumber>-1;
        if(condition) this.score=possibleScore;
        return condition;
    };
    this.hasFourKind=function() {
        this.score=0;
        var possibleScore=0;
        var fullHand=this.getFullHand();
        for(var x=0; x<fullHand.length; x++) {
            possibleScore=0;
            var currentCount=0;
            for(var y=0; y<fullHand.length; y++) {
                if(fullHand[x].value===fullHand[y].value) {
                    possibleScore+=fullHand[x].value;
                    currentCount++;
                }
            }
            if(currentCount>4) {
                console.log("This should never happen");
            }
            else if(currentCount===4) {
                this.score=possibleScore;
                return true;
            }
        }
        return false;
    };
    this.hasStraightFlush=function() {
        return this.hasStraight()&&this.hasFlush();
    };
    this.hasRoyalFlush=function() {
        var fullHand=this.getFullHand();
        return this.hasStraightFlush()&&hasCardWithValue(fullHand, 1)&&hasCardWithValue(fullHand, 10);
    };
    this.calculateHandScore=function() {
        var score=0;
        // Check for royal flushes
        if(this.hasRoyalFlush()) {
            console.log("Royal Flush");
            score=10;
        }
        // Check for straight flushes
        else if(this.hasStraightFlush()) {
            console.log("Straight Flush");
            score=9;
        }
        // Four of a kind
        else if(this.hasFourKind()) {
            console.log("Four of a Kind");
            score=8;
        }
        // Full house
        else if(this.hasFullHouse()) {
            console.log("Full House");
            score=7;
        }
        // Flush
        else if(this.hasFlush()) {
            console.log("Flush");
            score=6;
        }
        // Straight
        else if(this.hasStraight()) {
            console.log("Straight");
            score=5;
        }
        // Three of a kind
        else if(this.hasThreeKind()) {
            console.log("Three of a Kind");
            score=4;
        }
        // Two pair
        else if(this.hasTwoPair()) {
            console.log("Two Pair");
            score=3;
        }
        // One pair
        else if(this.hasPair()) {
            console.log("Pair");
            score=2;
        }
        // High card
        else {
            this.hasHighCard();
            console.log("High Card");
            score=1;
        }
        return score;
    };
}
function hasCardWithValue(haystack, value) {
    if(value>14||value<1) return false;
    // Ace is both high and low
    if(value===14) value=1;
    for(var i=0; i<haystack.length; i++) {
        if(haystack[i].value===value) return true;
    }
    return false;
}