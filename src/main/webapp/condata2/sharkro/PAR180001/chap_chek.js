//var code = "s1015";

var tot_page=new Array();


tot_page[1] = 12;
tot_page[2] = 13;
tot_page[3] = 11;
tot_page[4] = 16;
tot_page[5] = 15;
tot_page[6] = 14;
tot_page[7] = 13;
tot_page[8] = 15;
tot_page[9] = 12;
tot_page[10] = 12;
tot_page[11] = 15;
tot_page[12] = 14;
tot_page[13] = 17;
tot_page[14] = 15;
tot_page[15] = 13;
tot_page[16] = 12;
tot_page[17] = 14;
tot_page[18] = 17;
tot_page[19] = 16;
tot_page[20] = 12;
tot_page[21] = 15;
tot_page[22] = 14;
tot_page[23] = 14;
tot_page[24] = 14;
tot_page[25] = 12;
tot_page[26] = 14;
tot_page[27] = 15;
tot_page[28] = 12;
tot_page[29] = 14;
tot_page[30] = 13;


var menu_page = new Array();
var main_index = new Array();
menu_page[1] = [2, 3, 4, 6, 8, 9, 10, 11, 12];
main_index[1] = [1];

menu_page[2] = [2, 3, 4, 9, 10, 11, 12, 13];
main_index[2] = [1];

menu_page[3] = [2, 3, 4, 7, 8, 9, 10, 11];
main_index[3] = [1];

menu_page[4] = [2, 3, 4, 9, 12, 13, 14, 15, 16];
main_index[4] = [1];

menu_page[5] = [2, 3, 4, 7, 12, 13, 14, 15];
main_index[5] = [1];

menu_page[6] = [2, 3, 4, 9, 11, 12, 13, 14];
main_index[6] = [1];

menu_page[7] = [2, 3, 4, 5, 7, 8, 10, 11, 12, 13];
main_index[7] = [1];

menu_page[8] = [2, 3, 4, 6, 10, 12, 13, 14, 15];
main_index[8] = [1];

menu_page[9] = [2, 3, 4, 5, 9, 10, 11, 12];
main_index[9] = [1];

menu_page[10] = [2, 3, 4, 6, 9, 10, 11, 12];
main_index[10] = [1];

menu_page[11] = [2, 3, 4, 6, 10, 12, 13, 14, 15];
main_index[11] = [1];

menu_page[12] = [2, 3, 4, 5, 8, 10, 11, 12, 13, 14];
main_index[12] = [1];

menu_page[13] = [2, 3, 4, 5, 11, 14, 15, 16, 17];
main_index[13] = [1];

menu_page[14] = [2, 3, 4, 6, 7, 12, 13, 14, 15];
main_index[14] = [1];

menu_page[15] = [2, 3, 4, 5, 7, 10, 11, 12, 13];
main_index[15] = [1];

menu_page[16] = [2, 3, 4, 5, 8, 9, 10, 11, 12];
main_index[16] = [1];

menu_page[17] = [2, 3, 4, 7, 8, 11, 12, 13, 14];
main_index[17] = [1];

menu_page[18] = [2, 3, 4, 7, 8, 14, 15, 16, 17];
main_index[18] = [1];

menu_page[19] = [2, 3, 4, 7, 11, 13, 14, 15, 16];
main_index[19] = [1];

menu_page[20] = [2, 3, 4, 6, 7, 9, 10, 11, 12];
main_index[20] = [1];

menu_page[21] = [2, 3, 4, 6, 10, 12, 13, 14, 15];
main_index[21] = [1];

menu_page[22] = [2, 3, 4, 7, 10, 11, 12, 13, 14];
main_index[22] = [1];

menu_page[23] = [2, 3, 4, 7, 10, 11, 12, 13, 14];
main_index[23] = [1];

menu_page[24] = [2, 3, 4, 6, 8, 11, 12, 13, 14];
main_index[24] = [1];

menu_page[25] = [2, 3, 4, 5, 7, 9, 10, 11, 12];
main_index[25] = [1];

menu_page[26] = [2, 3, 4, 5, 9, 11, 12, 13, 14];
main_index[26] = [1];

menu_page[27] = [2, 3, 4, 5, 9, 12, 13, 14, 15];
main_index[27] = [1];

menu_page[28] = [2, 3, 4, 5, 7, 9, 10, 11, 12];
main_index[28] = [1];

menu_page[29] = [2, 3, 4, 6, 8, 11, 12, 13, 14];
main_index[29] = [1];

menu_page[30] = [2, 3, 4, 8, 9, 10, 11, 12, 13];
main_index[30] = [1];

var startFrameArr = new Array();
startFrameArr[1] = [];
startFrameArr[1][2] = [224];
startFrameArr[1][4] = [558];
startFrameArr[1][5] = [296];
startFrameArr[1][6] = [292];
startFrameArr[1][7] = [225];
startFrameArr[1][9] = [640];

startFrameArr[2] = [];
startFrameArr[2][2] = [224];
startFrameArr[2][4] = [337];
startFrameArr[2][6] = [640];

startFrameArr[3] = [];
startFrameArr[3][2] = [224];
startFrameArr[3][4] = [368];
startFrameArr[3][5] = [163];
startFrameArr[3][7] = [640];

startFrameArr[4] = [];
startFrameArr[4][2] = [224];
startFrameArr[4][4] = [336];
startFrameArr[4][7] = [640];

startFrameArr[5] = [];
startFrameArr[5][4] = [340];
startFrameArr[5][5] = [165];
startFrameArr[5][6] = [16];
startFrameArr[5][7] = [16];
startFrameArr[5][8] = [16];
startFrameArr[5][10] = [640];

startFrameArr[6] = [];
startFrameArr[6][4] = [349];
startFrameArr[6][5] = [16];
startFrameArr[6][6] = [17];
startFrameArr[6][7] = [143];
startFrameArr[6][8] = [16];
startFrameArr[6][9] = [16];
startFrameArr[6][11] = [640];

startFrameArr[7] = [];
startFrameArr[7][2] = [224];
startFrameArr[7][4] = [405];
startFrameArr[7][5] = [292];
startFrameArr[7][7] = [640];

startFrameArr[8] = [];
startFrameArr[8][4] = [321];
startFrameArr[8][5] = [137];
startFrameArr[8][6] = [130];
startFrameArr[8][7] = [128];
startFrameArr[8][8] = [183];
startFrameArr[8][10] = [640];

startFrameArr[9] = [];
startFrameArr[9][4] = [405];
startFrameArr[9][5] = [143];
startFrameArr[9][6] = [222];
startFrameArr[9][8] = [640];

startFrameArr[10] = [];
startFrameArr[10][2] = [224];
startFrameArr[10][4] = [335];
startFrameArr[10][5] = [143];
startFrameArr[10][6] = [165];
startFrameArr[10][7] = [177];
startFrameArr[10][9] = [640];

startFrameArr[11] = [];
startFrameArr[11][2] = [224];
startFrameArr[11][4] = [371];
startFrameArr[11][7] = [640];

startFrameArr[12] = [];
startFrameArr[12][4] = [408];
startFrameArr[12][5] = [191];
startFrameArr[12][7] = [640];

startFrameArr[13] = [];
startFrameArr[13][4] = [362];
startFrameArr[13][5] = [165];
startFrameArr[13][6] = [194];
startFrameArr[13][7] = [148];
startFrameArr[13][9] = [640];

startFrameArr[14] = [];
startFrameArr[14][2] = [224];
startFrameArr[14][6] = [16];
startFrameArr[14][7] = [16];
startFrameArr[14][8] = [16];
startFrameArr[14][10] = [640];

startFrameArr[15] = [];
startFrameArr[15][2] = [224];
startFrameArr[15][4] = [421];
startFrameArr[15][8] = [640];

startFrameArr[16] = [];
startFrameArr[16][2] = [224];
startFrameArr[16][4] = [425];
startFrameArr[16][6] = [16];
startFrameArr[16][7] = [16];
startFrameArr[16][9] = [640];

startFrameArr[17] = [];
startFrameArr[17][4] = [405];
startFrameArr[17][5] = [224];
startFrameArr[17][7] = [640];

startFrameArr[18] = [];
startFrameArr[18][2] = [224];
startFrameArr[18][4] = [350];
startFrameArr[18][5] = [158];
startFrameArr[18][6] = [141];
startFrameArr[18][7] = [181];
startFrameArr[18][8] = [146];
startFrameArr[18][10] = [640];

startFrameArr[19] = [];
startFrameArr[19][4] = [373];
startFrameArr[19][5] = [167];
startFrameArr[19][6] = [149];
startFrameArr[19][7] = [183];
startFrameArr[19][8] = [141];
startFrameArr[19][9] = [161];
startFrameArr[19][11] = [640];


startFrameArr[20] = [];
startFrameArr[20][2] = [224];
startFrameArr[20][4] = [433];
startFrameArr[20][5] = [213];
startFrameArr[20][6] = [179];
startFrameArr[20][7] = [173];
startFrameArr[20][8] = [179];
startFrameArr[20][9] = [213];
startFrameArr[20][11] = [640];

startFrameArr[21] = [];
startFrameArr[21][2] = [224];
startFrameArr[21][4] = [433];
startFrameArr[21][5] = [213];
startFrameArr[21][6] = [179];
startFrameArr[21][7] = [173];
startFrameArr[21][8] = [179];
startFrameArr[21][9] = [213];
startFrameArr[21][11] = [640];

startFrameArr[22] = [];
startFrameArr[22][2] = [224];
startFrameArr[22][4] = [433];
startFrameArr[22][5] = [213];
startFrameArr[22][6] = [179];
startFrameArr[22][7] = [173];
startFrameArr[22][8] = [179];
startFrameArr[22][9] = [213];
startFrameArr[22][11] = [640];

startFrameArr[23] = [];
startFrameArr[23][2] = [224];
startFrameArr[23][4] = [433];
startFrameArr[23][5] = [213];
startFrameArr[23][6] = [179];
startFrameArr[23][7] = [173];
startFrameArr[23][8] = [179];
startFrameArr[23][9] = [213];
startFrameArr[23][11] = [640];

startFrameArr[24] = [];
startFrameArr[24][2] = [224];
startFrameArr[24][4] = [433];
startFrameArr[24][5] = [213];
startFrameArr[24][6] = [179];
startFrameArr[24][7] = [173];
startFrameArr[24][8] = [179];
startFrameArr[24][9] = [213];
startFrameArr[24][11] = [640];

startFrameArr[25] = [];
startFrameArr[25][2] = [224];
startFrameArr[25][4] = [433];
startFrameArr[25][5] = [213];
startFrameArr[25][6] = [179];
startFrameArr[25][7] = [173];
startFrameArr[25][8] = [179];
startFrameArr[25][9] = [213];
startFrameArr[25][11] = [640];

startFrameArr[26] = [];
startFrameArr[26][2] = [224];
startFrameArr[26][4] = [433];
startFrameArr[26][5] = [213];
startFrameArr[26][6] = [179];
startFrameArr[26][7] = [173];
startFrameArr[26][8] = [179];
startFrameArr[26][9] = [213];
startFrameArr[26][11] = [640];

startFrameArr[27] = [];
startFrameArr[27][2] = [224];
startFrameArr[27][4] = [433];
startFrameArr[27][5] = [213];
startFrameArr[27][6] = [179];
startFrameArr[27][7] = [173];
startFrameArr[27][8] = [179];
startFrameArr[27][9] = [213];
startFrameArr[27][11] = [640];

startFrameArr[28] = [];
startFrameArr[28][2] = [224];
startFrameArr[28][4] = [433];
startFrameArr[28][5] = [213];
startFrameArr[28][6] = [179];
startFrameArr[28][7] = [173];
startFrameArr[28][8] = [179];
startFrameArr[28][9] = [213];
startFrameArr[28][11] = [640];

startFrameArr[29] = [];
startFrameArr[29][2] = [224];
startFrameArr[29][4] = [433];
startFrameArr[29][5] = [213];
startFrameArr[29][6] = [179];
startFrameArr[29][7] = [173];
startFrameArr[29][8] = [179];
startFrameArr[29][9] = [213];
startFrameArr[29][11] = [640];

startFrameArr[30] = [];
startFrameArr[30][2] = [224];
startFrameArr[30][4] = [433];
startFrameArr[30][5] = [213];
startFrameArr[30][6] = [179];
startFrameArr[30][7] = [173];
startFrameArr[30][8] = [179];
startFrameArr[30][9] = [213];
startFrameArr[30][11] = [640];

var captureTimeArr = new Array();

captureTimeArr[1] = [];
captureTimeArr[1][4] = [515];

captureTimeArr[2] = [];
captureTimeArr[2][4] = [352];

captureTimeArr[3] = [];
captureTimeArr[3][4] = [358];
captureTimeArr[3][5] = [163];

captureTimeArr[4] = [];
captureTimeArr[4][4] = [205];
captureTimeArr[4][5] = [""];

captureTimeArr[5] = [];
captureTimeArr[5][4] = [71];
captureTimeArr[5][5] = [102];

captureTimeArr[6] = [];
captureTimeArr[6][6] = [30];
captureTimeArr[6][8] = [506];

captureTimeArr[7] = [];
captureTimeArr[7][4] = [231];
captureTimeArr[7][5] = [168];

captureTimeArr[8] = [];
captureTimeArr[8][4] = [44];
captureTimeArr[8][7] = [121];
captureTimeArr[8][8] = [117];

captureTimeArr[9] = [];
captureTimeArr[9][4] = [30];
captureTimeArr[9][5] = [191];
captureTimeArr[9][6] = [94];

captureTimeArr[10] = [];
captureTimeArr[10][4] = [60];
captureTimeArr[10][5] = [44];
captureTimeArr[10][6] = [167];
captureTimeArr[10][7] = [96];

captureTimeArr[11] = [];
captureTimeArr[11][4] = [""];

captureTimeArr[12] = [];
captureTimeArr[12][4] = [498];
captureTimeArr[12][5] = [120];

captureTimeArr[13] = [];
captureTimeArr[13][4] = [67];
captureTimeArr[13][6] = [465];

captureTimeArr[14] = [];
captureTimeArr[14][4] = [""];

captureTimeArr[15] = [];
captureTimeArr[15][4] = [""];
captureTimeArr[15][5] = [""];

captureTimeArr[16] = [];
captureTimeArr[16][4] = [211];

captureTimeArr[17] = [];
captureTimeArr[17][4] = [231];

captureTimeArr[18] = [];
captureTimeArr[18][4] = [126];
captureTimeArr[18][5] = [111];
captureTimeArr[18][6] = [186];
captureTimeArr[18][7] = [66];
captureTimeArr[18][8] = [189];

captureTimeArr[19] = [];
captureTimeArr[19][4] = [85];
captureTimeArr[19][5] = [90];
captureTimeArr[19][6] = [72];
captureTimeArr[19][7] = [160];
captureTimeArr[19][8] = [101];
captureTimeArr[19][9] = [28];

captureTimeArr[20] = [];
captureTimeArr[20][4] = [280];
captureTimeArr[20][5] = [273];
captureTimeArr[20][6] = [85];
captureTimeArr[20][7] = [75];
captureTimeArr[20][8] = [181];
captureTimeArr[20][9] = [72];


var syncTimeArr = new Array();

syncTimeArr[1] = [];
syncTimeArr[1][2] = ["",0,1];
syncTimeArr[1][4] = ["",21,25,31,99,123,147,216,271,357,373,438,447];
syncTimeArr[1][5] = ["",0,1,43,109,125,182,204,258];
syncTimeArr[1][6] = ["",0,10,70,111,155,236,255,327];
syncTimeArr[1][7] = ["",0,21,133,196,305,366];
syncTimeArr[1][9] = ["",0,1];

syncTimeArr[2] = [];
syncTimeArr[2][2] = ["",0,1];
syncTimeArr[2][4] = ["", 1,31,35,75,120, 127,136,143,197,199, 212,219,228, 241, 269,305,309, 347, 353,357,380,402, 424,433,445,454,502, 562,566,579,595,606,676, 680]
syncTimeArr[2][6] = ["",0,1];

syncTimeArr[3] = [];
syncTimeArr[3][2] = ["",0,1];
syncTimeArr[3][4] = ["",1,10,188,190,204,625];
syncTimeArr[3][5] = ["",0,1,254,566];
syncTimeArr[3][7] = ["",0,1];

syncTimeArr[4] = [];
syncTimeArr[4][2] = ["",0,1];
syncTimeArr[4][4] = ["",0,1,40,71,119];
syncTimeArr[4][7] = ["",0,1];

syncTimeArr[5] = [];
syncTimeArr[5][4] = ["",1,10,43,52,72,85,129,167,201,257,284,321,382,424,445,458,479,506,530,540,548,560];
syncTimeArr[5][5] = ["",0,1,44,58,66,103,114,118,126,151,178];
syncTimeArr[5][6] = ["",0,1,5,39,72,89,129,145,174,185,231,240];
syncTimeArr[5][7] = ["",0,1,31,39,121,129];
syncTimeArr[5][8] = ["",0,1,23,183,195];
syncTimeArr[5][10] = ["",0,1];

syncTimeArr[6] = [];
syncTimeArr[6][4] = ["",1,10,46,73,255,329];
syncTimeArr[6][5] = ["",0,1,13,29,240,282];
syncTimeArr[6][6] = ["",0,1,8,16];
syncTimeArr[6][7] = ["",0,1,7,60];
syncTimeArr[6][8] = ["",0,1];
syncTimeArr[6][9] = ["",0,1];
syncTimeArr[6][11] = ["",0,1];

syncTimeArr[7] = [];
syncTimeArr[7][2] = ["",0,1];
syncTimeArr[7][4] = ["",0,1,7,45,82,134,139,171,183,186,190,207,231,293];
syncTimeArr[7][5] = ["",0,1,10,16,69,118,170,193,205];
syncTimeArr[7][7] = ["",0,1];

syncTimeArr[8] = [];
syncTimeArr[8][4] = ["",0,1,37,44];
syncTimeArr[8][5] = ["",0,1,2,10];
syncTimeArr[8][6] = ["",0,1,2];
syncTimeArr[8][7] = ["",0,1,2];
syncTimeArr[8][8] = ["",0,1,20,36];
syncTimeArr[8][10] = ["",0,1];

syncTimeArr[9] = [];
syncTimeArr[9][4] = ["",0,1,5];
syncTimeArr[9][5] = ["",0,1,32,192,235,331,363];
syncTimeArr[9][6] = ["",0,1,9,103];
syncTimeArr[9][8] = ["",0,1];

syncTimeArr[10] = [];
syncTimeArr[10][2] = ["",0,1];
syncTimeArr[10][4] = ["",0,1,33];
syncTimeArr[10][5] = ["",0,1,14];
syncTimeArr[10][6] = ["",0,1,18,82];
syncTimeArr[10][7] = ["",0,1,4,97,196];
syncTimeArr[10][9] = ["",0,1];

syncTimeArr[11] = [];
syncTimeArr[11][2] = ["",0,1];
syncTimeArr[11][4] = ["",1,10,36,46,78,218,244,277,315,449,529,573,601,740,840,903];
syncTimeArr[11][7] = ["",0,1];

syncTimeArr[12] = [];
syncTimeArr[12][4] = ["",0,1,43,87,113,122,128,322,324,498,513];
syncTimeArr[12][5] = ["",0,1,13,121,175];
syncTimeArr[12][7] = ["",0,1];

syncTimeArr[13] = [];
syncTimeArr[13][4] = ["",0,1,15];
syncTimeArr[13][5] = ["",0,1,33];
syncTimeArr[13][6] = ["",0,1,76,102,282,378,465];
syncTimeArr[13][7] = ["",0,1,6];
syncTimeArr[13][9] = ["",0,1];

syncTimeArr[14] = [];
syncTimeArr[14][2] = ["",0,1];
syncTimeArr[14][6] = ["",0,1,11];
syncTimeArr[14][7] = ["",0,1,11];;
syncTimeArr[14][8] = ["",0,1,11];
syncTimeArr[14][10] = ["",0,1];

syncTimeArr[15] = [];
syncTimeArr[15][2] = ["",0,1];
syncTimeArr[15][4] = ["",0,1];
syncTimeArr[15][8] = ["",0,1];

syncTimeArr[16] = [];
syncTimeArr[16][2] = ["",0,1];
syncTimeArr[16][4] = ["",0,1];
syncTimeArr[16][6] = ["",0,1,33,98,102,107,113,117,158,185,218,223,252,263,302,357,419];
syncTimeArr[16][7] = ["",0,1,120,155,187,247];
syncTimeArr[16][9] = ["",0,1];

syncTimeArr[17] = [];
syncTimeArr[17][2] = ["",0,4];
syncTimeArr[17][4] = ["",0,1,8,22,27,31,34,37,40,42,59,71,81,93,129,139,148,162,176,188];
syncTimeArr[17][5] = ["",0,1,7,12,17,28,31,36,54,60,64,66,75,	104,108,119,123,138.5,	139,140,145,161,166,173,182,195,201,205,	219,230,241,251,300,305,317,335,346,358,372,384,406,457,464,466,505 ];
syncTimeArr[17][7] = ["",0,1,104];

syncTimeArr[18] = [];
syncTimeArr[18][2] = ["",0,1];
syncTimeArr[18][4] = ["",0,1, 51,127,190];
syncTimeArr[18][5] = ["",0,1,10,81,111,190,240,281];
syncTimeArr[18][6] = ["",0,1,12];
syncTimeArr[18][7] = ["",0,2,13,34,66,121];
syncTimeArr[18][8] = ["",0,4,21,52,189];

syncTimeArr[19] = [];
syncTimeArr[19][4] = ["",0,1,13,27,37,45,49,61,85,86];
syncTimeArr[19][5] = ["",0,1,10,11,13,16,23,24,25,38,40,90,97 ];
syncTimeArr[19][6] = ["",0,1,3,25,30,33,37,48,56];
syncTimeArr[19][7] = ["",0,1,4,12,52,59,60,80,161,177,229,301];
syncTimeArr[19][8] = ["",0,1,2,4,20,34,60,85];
syncTimeArr[19][9] = ["",0,1,10];
syncTimeArr[19][11] = ["",0,1];

syncTimeArr[20] = [];
syncTimeArr[20][2] = ["",0,1];
syncTimeArr[20][4] = ["",0,1,62,121,163,208,245,261,280,330,352,368,394,430,467];
syncTimeArr[20][5] = ["",0,1,228,274,290];
syncTimeArr[20][6] = ["",0,1,20,86,118,143];
syncTimeArr[20][7] = ["",0,1,19,76];
syncTimeArr[20][8] = ["",0,1,7,36,72,128,144,182,245,289,320,356];
syncTimeArr[20][9] = ["",0,2,13,41];
syncTimeArr[20][11] = ["",0,1];