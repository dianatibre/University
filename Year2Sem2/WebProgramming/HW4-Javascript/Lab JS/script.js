var tileClicked = 0;
var topPosFir = 0;
var leftPosFir = 0;
var topPosSec = 0;
var leftPosSec = 0;
var lastClickedID = '';

function clicked(ID) {
    var d = document.getElementById(ID);
    d.classList.add("glow");
    //lastClickedID = ID
    tileClicked += 1;
    if (lastClickedID != '') {
        //myId = lastClickedID;
        bothClicked(lastClickedID,ID);
        //lastClickedID = ''
    }
    else {
        lastClickedID = ID
    }
}

function bothClicked(firstID, secondID) {
    var d1 = document.getElementById(firstID);
    topPosFir = d1.style.top;
    leftPosFir = d1.style.left;
    var d2 = document.getElementById(secondID);
    topPosSec = d2.style.top;
    leftPosSec = d2.style.left;
    //d1.classList.add("glow");
    //d2.classList.add("glow");
    d1.style.position = "absolute";
    d1.style.top = topPosSec;//+'px';
    d1.style.left = leftPosSec;//+'px';
    d2.style.position = "absolute";
    d2.style.top = topPosFir;//+'px';
    d2.style.left = leftPosFir;//+'px';
    d1.classList.remove("glow");
    d2.classList.remove("glow");
    lastClickedID = '';
    tileClicked = 0;
    checkWin()
}

function checkWin() {
    if (
        document.getElementById('piece-1').style.top == '0px' && document.getElementById('piece-1').style.left == '0px' &&
        document.getElementById('piece-2').style.top == '0px' && document.getElementById('piece-2').style.left == '200px' &&
        document.getElementById('piece-3').style.top == '0px' && document.getElementById('piece-3').style.left == '400px' &&
        document.getElementById('piece-4').style.top == '200px' && document.getElementById('piece-4').style.left == '0px' &&
        document.getElementById('piece-5').style.top == '200px' && document.getElementById('piece-5').style.left == '200px' &&
        document.getElementById('piece-6').style.top == '200px' && document.getElementById('piece-6').style.left == '400px' &&
        document.getElementById('piece-7').style.top == '400px' && document.getElementById('piece-7').style.left == '0px' &&
        document.getElementById('piece-8').style.top == '400px' && document.getElementById('piece-8').style.left == '200px' &&
        document.getElementById('piece-9').style.top == '400px' && document.getElementById('piece-9').style.left == '400px'   
    ) {
        document.getElementById('text').innerHTML = "Well done!"
        
        lockImage();
    }
}

function lockImage() {
    var ids = ['piece-1', 'piece-2', 'piece-3', 'piece-4', 'piece-5', 'piece-6', 'piece-7', 'piece-8', 'piece-9']
    var i;
    for (i=0; i<9; i++) {
        document.getElementById(ids[i]).onclick = null;
    }

}