package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/*
Allegheny County, PA
 */


public class PAAlleghenyCountyParser extends GroupBestParser {

  public PAAlleghenyCountyParser() {
    super(new PAAlleghenyCountyAParser(),
          new PAAlleghenyCountyBParser(),
          new PAAlleghenyCountyCParser(),
          new GroupBlockParser(),
          new PAAlleghenyCountyDParser());
  }

  static final Properties GPS_TABLE_LOOKUP = buildCodeTable(new String[]{
      "1 80TH DIVISION MONUMENT",             "+40.594236,-79.996583",
      "2 ALLEGHENY GV",                       "+40.581689,-79.979976",
      "3 AMBRIDGE GV",                        "+40.613208,-80.019015",
      "4 BABBLEBROOK GV",                     "+40.594209,-79.997694",
      "10050 BABCOCK BLVD",                   "+40.594247,-79.997322",
      "1300 BASKETBALL CT",                   "+40.604104,-80.010070",
      "5 BEAVER GV",                          "+40.599679,-79.996518",
      "6 BELLEVUE GV",                        "+40.607563,-80.017302",
      "8 BEVERIDGE GV",                       "+40.588024,-79.984308",
      "9 BLACKROCK GV",                       "+40.584742,-79.989527",
      "10 BUFFALO GV",                        "+40.600072,-80.005087",
      "1 BULL PEN RD",                        "+40.598074,-80.007087",
      "13 CONNOLLY GV",                       "+40.593132,-79.994704",
      "14 COTTAGE GROVE GV",                  "+40.591380,-79.997665",
      "15 COTTONOODS GV",                     "+40.590932,-79.999122",
      "171 DEER BROWSE NO 1 FD",              "+40.604104,-80.010070",
      "172 DEER BROWSE NO 2 FD",              "+40.604104,-80.010070",
      "173 DEER BROWSE NO 3 FD",              "+40.604104,-80.010070",
      "181 DEVILS ELBOW NO 1 FD",             "+40.586653,-79.997589",
      "182 DEVILS ELBOW NO 2 FD",             "+40.586049,-79.997697",
      "20 DONNEVALE GV",                      "+40.590071,-79.986202",
      "21 DONORA GV",                         "+40.589056,-79.985001",
      "23 DUQUESNE GV",                       "+40.591059,-79.999715",
      "940 E INGOMAR RD",                     "+40.592813,-80.011393",
      "1370 E INGOMAR RD",                    "+40.593387,-79.998900",
      "24 ELWOOD GV",                         "+40.587678,-79.985792",
      "25 EMSWORTH GV",                       "+40.594264,-80.011437",
      "26 ERIE GV",                           "+40.594619,-80.005245",
      "27 ETNA GV",                           "+40.598723,-80.003182",
      "28 FAWN GV",                           "+40.585645,-79.990843",
      "1 FILTRATION PLANT",                   "+40.588648,-79.995097",
      "30 FLANDERS GV",                       "+40.601897,-80.013535",
      "31 FOREST GLEN GV",                    "+40.591867,-79.995539",
      "32 FOREST VALE GV",                    "+40.591007,-79.995909",
      "33 FRANKLIN GV",                       "+40.590525,-79.992753",
      "34 FRAZIER GV",                        "+40.596843,-80.001491",
      "35 GARNER GV",                         "+40.594682,-80.007674",
      "1 GIRL SCOUT CABIN",                   "+40.596480,-80.016474",
      "120 GOLD STAR GV",                     "+40.593682,-80.003863",
      "1 GREEN HOUSE",                        "+40.241283,-79.850003",
      "37 GREENTREE GV",                      "+40.593977,-80.012007",
      "38 HAMPTON GV",                        "+40.599189,-80.004339",
      "39 HARMAR GV",                         "+40.588353,-79.990687",
      "40 HARMONY GV",                        "+40.606262,-80.008701",
      "13 HAYLOFT GV",                        "+40.246820,-79.854076",
      "41 HAYSVILLE GV",                      "+40.593085,-80.012863",
      "43 HENDERSON GV",                      "+40.587378,-79.997386",
      "102 HEREFORD DR",                      "+40.241508,-79.852764",
      "255 HEREFORD DR",                      "+40.243700,-79.855664",
      "351 HEREFORD DR",                      "+40.244907,-79.856178",
      "355 HEREFORD DR",                      "+40.244806,-79.856700",
      "400 HEREFORD DR",                      "+40.245323,-79.855144",
      "405 HEREFORD DR",                      "+40.245632,-79.857282",
      "420 HEREFORD DR",                      "+40.245727,-79.855301",
      "565 HEREFORD DR",                      "+40.246712,-79.856823",
      "705 HEREFORD DR",                      "+40.247758,-79.854587",
      "776 HEREFORD DR",                      "+40.245848,-79.851227",
      "778 HEREFORD DR",                      "+40.246256,-79.851468",
      "780 HEREFORD DR",                      "+40.247032,-79.853717",
      "1079 HEREFORD DR",                     "+40.251614,-79.852436",
      "46 HILLSIDE GV",                       "+40.597889,-80.002241",
      "47 HONEYSUCKLE GV",                    "+40.588459,-79.988082",
      "49 IDAHO GV",                          "+40.603627,-80.001128",
      "50 INGOMAR GV",                        "+40.592086,-79.997305",
      "51 IOWA GV",                           "+40.609193,-80.008306",
      "1 JC STONE FIELD CONCESSION STAND",    "+40.596349,-80.011245",
      "1300 JC STONE FIELD FD",               "+40.604104,-80.010070",
      "1 JC STONE FIELD LOCKER ROOMS",        "+40.596676,-80.011476",
      "52 JEANETTE GV",                       "+40.588244,-79.986668",
      "53 JEFFERSON GV",                      "+40.594910,-80.008709",
      "54 JUNIATA GV",                        "+40.592860,-79.995510",
      "55 KILBUCK GV",                        "+40.594533,-80.010420",
      "22 KOLICH GV",                         "+40.599051,-80.002470",
      "10150 KUMMER RD",                      "+40.606573,-80.021319",
      "12000 KUMMER RD",                      "+40.615932,-80.023429",
      "160 LAKE DR",                          "+40.616268,-80.052656",
      "10421 LAKE SHORE DR",                  "+40.606765,-80.014451",
      "57 LAKESHORE GV",                      "+40.600336,-80.004196",
      "58 LAKEVIEW GV",                       "+40.607002,-80.004347",
      "121 LATHAM GV",                        "+40.601016,-80.004552",
      "59 LATROBE GV",                        "+40.605855,-80.009621",
      "60 LEDGEWOOD GV",                      "+40.592022,-80.013099",
      "61 LEET GV",                           "+40.594719,-80.009601",
      "64 LILAC GV",                          "+40.587891,-79.987143",
      "65 LITTLE MAMA GV",                    "+40.594849,-80.001889",
      "66 LONE PINE GV",                      "+40.587225,-79.982811",
      "67 LONG VIEW GV",                      "+40.589503,-79.995368",
      "68 MAIN GV",                           "+40.582065,-79.978890",
      "69 MARS GV",                           "+40.604956,-80.011265",
      "71 MASSACHUSETTES GV",                 "+40.591949,-79.992531",
      "72 MCCANDLESS GV",                     "+40.588501,-79.996550",
      "635 MCKINNEY RD",                      "+40.612512,-80.021679",
      "1100 MCKINNEY RD",                     "+40.614804,-80.016080",
      "4 MEADOW GV",                          "+40.246114,-79.856832",
      "73 MILL GV",                           "+40.610543,-80.018862",
      "74 MILLER GV",                         "+40.617172,-80.022691",
      "76 MONTANA GV",                        "+40.610972,-80.009681",
      "77 MOON GV",                           "+40.591208,-80.000846",
      "1 NATURALISTS HOUSE",                  "+40.620996,-80.030526",
      "1 NATURE CENTER RENTAL HOME 1",        "+40.621368,-80.030826",
      "1 NATURE CENTER RENTAL HOME 2",        "+40.621403,-80.030614",
      "80 NINEBARK GV",                       "+40.592202,-79.997842",
      "81 NORTH CAROLINA GV",                 "+40.600596,-79.999334",
      "82 NORTH DAKOTA GV",                   "+40.619417,-80.025359",
      "1 NORTH PARK BASKETBALL COURTS",       "+40.617151,-80.024702",
      "1 NORTH PARK DEVIL'S ELBOW FIELD",     "+40.585184,-79.996928",
      "1 NORTH PARK DOG PARK",                "+40.608529,-80.017005",
      "1 NORTH PARK DONORA FIELD",            "+40.589543,-79.984196",
      "1 NORTH PARK HORSE SHOW RING",         "+40.604883,-80.025287",
      "1 NORTH PARK JC STONE FIELD",          "+40.597168,-80.010957",
      "1 NORTH PARK LAKEVIEW BASEBALL FIELD", "+40.607806,-80.003967",
      "1 NORTH PARK LONE PINE FIELD",         "+40.588583,-79.983509",
      "1 NORTH PARK MARSHALL ISLAND",         "+40.613945,-80.020965",
      "1 NORTH PARK MCKINNEY FIELD",          "+40.620205,-80.048465",
      "1 NORTH PARK MILLFIELD",               "+40.610920,-80.018166",
      "1 NORTH PARK PIE TRAYNOR FIELD",       "+40.587293,-79.991162",
      "1 NORTH PARK PIE TRAYNOR SOCCER FIELD","+40.586188,-79.992466",
      "1 NORTH PARK PRACTICE HORSE SHOW RING","+40.604222,-80.026686",
      "1 NORTH PARK SCHWARTZ FIELD 1",        "+40.618329,-80.048070",
      "1 NORTH PARK SCHWARTZ FIELD 2",        "+40.617928,-80.049085",
      "1 NORTH PARK SENIOR CITIZENS SOFTBALL COMPLEX 1","+40.618687,-80.015655",
      "1 NORTH PARK SENIOR CITIZENS SOFTBALL COMPLEX 2","+40.618149,-80.016330",
      "1 NORTH PARK WOODS FIELD 1",           "+40.586382,-79.987634",
      "1 NORTH PARK WOODS FIELD 2",           "+40.587226,-79.987391",
      "1 NORTH PARK WOODS SOCCER FIELD",      "+40.585810,-79.987476",
      "1 NORTH PARK WYOMING FIELD",           "+40.614580,-80.012895",
      "1 NORTH PARK YONEST FIELD 1",          "+40.616943,-80.047066",
      "1 NORTH PARK YONEST FIELD 2",          "+40.616894,-80.047756",
      "5000 NORTH RIDGE DR",                  "+40.617145,-80.016962",
      "83 NORTH STAR GV",                     "+40.593968,-80.013934",
      "84 OAKDALE GV",                        "+40.599195,-79.999500",
      "85 OAKMONT GV",                        "+40.617581,-80.025602",
      "1 OBSERVATION TOWER",                  "+40.617525,-80.016327",
      "86 OLD CHERRY GV",                     "+40.589929,-79.994662",
      "1300 OLD FIREHOUSE GV",                "+40.595479,-80.011666",
      "1 OLD WATER FILTRATION PLANT",         "+40.612720,-80.021066",
      "87 OLYMPIA GV",                        "+40.616548,-80.021883",
      "88 ONTARIO GV",                        "+40.594372,-80.003692",
      "89 ORCHARD LAWN GV",                   "+40.606999,-80.015705",
      "90 OREGON GV",                         "+40.603350,-80.001001",
      "1 PARISH HILL RENTAL",                 "+40.602003,-80.015163",
      "91 PEARCE GV",                         "+40.606128,-80.007371",
      "10280 PEARCE MILL RD",                 "+40.604741,-80.004539",
      "10300 PEARCE MILL RD",                 "+40.605156,-80.005511",
      "10301 PEARCE MILL RD",                 "+40.604833,-80.007269",
      "92 PEEBLES GV",                        "+40.584350,-79.991381",
      "93 PERRY GV",                          "+40.585273,-79.987463",
      "1 PIE TRAYNOR FIELD CONCESSION STAND", "+40.586829,-79.990208",
      "94 PIGEON GV",                         "+40.613919,-80.027973",
      "95 PINE RIDGE GV",                     "+40.588900,-79.996356",
      "96 POINT GV",                          "+40.595582,-79.999800",
      "1300 RANCH HOUSE GV",                  "+40.604155,-80.023962",
      "1 RENTAL BUILDING",                    "+40.610579,-80.017293",
      "98 RICHLAND GV",                       "+40.593081,-79.997237",
      "99 ROCHESTER GV",                      "+40.605582,-80.011407",
      "100 ROOSEVELT GV",                     "+40.602905,-80.013395",
      "651 ROUND HILL RD",                    "+40.241950,-79.852350",
      "675 ROUND HILL RD",                    "+40.241373,-79.851063",
      "715 ROUND HILL RD",                    "+40.241248,-79.848946",
      "823 ROUND HILL RD",                    "+40.242835,-79.843698",
      "824 ROUND HILL RD",                    "+40.241217,-79.843081",
      "827 ROUND HILL RD",                    "+40.242866,-79.843019",
      "891 ROUND HILL RD",                    "+40.242017,-79.840233",
      "901 ROUND HILL RD",                    "+40.241666,-79.837695",
      "101 ROUND TOP GV",                     "+40.588702,-79.981664",
      "102 SCHOOL HOUSE GV",                  "+40.603496,-80.013632",
      "103 SESQUI GV",                        "+40.593510,-79.996518",
      "104 SHALER GV",                        "+40.595242,-80.006858",
      "105 SHARON GV",                        "+40.587968,-79.989686",
      "11 SUTTERSVILLE BRG",                  "+40.240711,-79.804383",
      "140 SUTTERSVILLE BRG",                 "+40.241633,-79.805559",
      "4660 SOUTH RIDGE DR",                  "+40.586884,-79.988420",
      "9901 SOUTH RIDGE RD",                  "+40.587516,-79.994407",
      "106 STONE RIDGE GV",                   "+40.582780,-79.981388",
      "108 SUPERIOR GV",                      "+40.596129,-80.000564",
      "2300 SURREY LN",                       "+40.290192,-79.826464",
      "2301 SURREY LN",                       "+40.290767,-79.825713",
      "2302 SURREY LN",                       "+40.290192,-79.826464",
      "2303 SURREY LN",                       "+40.290767,-79.825713",
      "2304 SURREY LN",                       "+40.290192,-79.826464",
      "2305 SURREY LN",                       "+40.290767,-79.825713",
      "2306 SURREY LN",                       "+40.289403,-79.826230",
      "2307 SURREY LN",                       "+40.290767,-79.825713",
      "2308 SURREY LN",                       "+40.289403,-79.826230",
      "2309 SURREY LN",                       "+40.290767,-79.825713",
      "2310 SURREY LN",                       "+40.289403,-79.826230",
      "2311 SURREY LN",                       "+40.290164,-79.825028",
      "2312 SURREY LN",                       "+40.289403,-79.826230",
      "2313 SURREY LN",                       "+40.290164,-79.825028",
      "2314 SURREY LN",                       "+40.289403,-79.826230",
      "2315 SURREY LN",                       "+40.290164,-79.825028",
      "2316 SURREY LN",                       "+40.289195,-79.824993",
      "2318 SURREY LN",                       "+40.289195,-79.824993",
      "2320 SURREY LN",                       "+40.289195,-79.824993",
      "2322 SURREY LN",                       "+40.289195,-79.824993",
      "2324 SURREY LN",                       "+40.289195,-79.824993",
      "2326 SURREY LN",                       "+40.289954,-79.824290",
      "2328 SURREY LN",                       "+40.289954,-79.824290",
      "2330 SURREY LN",                       "+40.289954,-79.824290",
      "2332 SURREY LN",                       "+40.289954,-79.824290",
      "2334 SURREY LN",                       "+40.289954,-79.824290",
      "991 TENNIS COURT LN",                  "+40.606064,-80.011137",
      "109 TRIPLE OAKS GV",                   "+40.588595,-79.987115",
      "110 TUPELO GV",                        "+40.591205,-79.992457",
      "111 VANDERGRIFT GV",                   "+40.618067,-80.023493",
      "9986 WALTER RD",                       "+40.596719,-80.012077",
      "10021 WALTER RD",                      "+40.594546,-80.013089",
      "800 WEST RIDGE RD",                    "+40.590380,-80.010032",
      "114 WEST VIEW GV",                     "+40.594406,-80.010920",
      "1005 WEXFORD PLAZA DR",                "+40.615690,-80.053360",
      "1025 WEXFORD PLAZA DR",                "+40.615287,-80.053383",
      "3100 WEXFORD RD",                      "+40.624464,-80.027265",
      "115 WILDWOOD GV",                      "+40.587187,-79.981769",
      "2154 WILDWOOD RD",                     "+40.592938,-79.982522",
      "116 WILLOWS GV",                       "+40.592003,-79.996031",
      "117 WISCONSIN GV",                     "+40.610413,-80.009922",
      "118 WOODS GV",                         "+40.585472,-79.986175",
      "119 WYOMING GV",                       "+40.615036,-80.012396",

      "SUTTERSVILLE BRG/DOUGLAS RUN RD",      "+40.241998,-79.806093",
      "SUTTERSVILLE BRG/MCPHERSON ST",        "+40.241998,-79.806093",
      
      "GAP - GREAT ALLEGHENY PASSAGE - 526 TWELE RD TRAIL ACCESS","+40.309757,-79.793342",
      "GAP - GREAT ALLEGHENY PASSAGE - 6737 SMITHFIELD ST TRAIL ACCESS","+40.318809,-79.811013",
      "GAP - GREAT ALLEGHENY PASSAGE - 900 E SMITHFIELD ST TRAIL ACCESS","+40.315528,-79.793851",
      "GAP - GREAT ALLEGHENY PASSAGE - ALLEGHENY CO WESTMORELAND CO LINE","+40.227078,-79.783262",
      "GAP - GREAT ALLEGHENY PASSAGE - APACHE SPRINGS   QUEEN ALIQUIPPA CAMP SITE","+40.288723,-79.777233",
      "GAP - GREAT ALLEGHENY PASSAGE - BLYTHEDALE PARK","+40.250872,-79.798651",
      "GAP - GREAT ALLEGHENY PASSAGE - BLYTHEDALE PARK TRAIL ACCESS","+40.252348,-79.798568",
      "GAP - GREAT ALLEGHENY PASSAGE - BLYTHEDALE PARK WOODEN BRIDGE","+40.254238,-79.797022",
      "GAP - GREAT ALLEGHENY PASSAGE - BOSTON PARK TRAIL ACCESS","+40.310436,-79.830908",
      "GAP - GREAT ALLEGHENY PASSAGE - BUENA VISTA BOAT LAUNCH ACCESS","+40.277242,-79.798170",
      "GAP - GREAT ALLEGHENY PASSAGE - BUENA VISTA ENERGY LLC","+40.293859,-79.794353",
      "GAP - GREAT ALLEGHENY PASSAGE - COVERED REST AREA","+40.303700,-79.806355",
      "GAP - GREAT ALLEGHENY PASSAGE - DADS LN TRAIL ACCESS","+40.260067,-79.791001",
      "GAP - GREAT ALLEGHENY PASSAGE - DRAKE ST TRAIL ACCESS","+40.312319,-79.825205",
      "GAP - GREAT ALLEGHENY PASSAGE - DRAVO CEMETARY","+40.289381,-79.778269",
      "GAP - GREAT ALLEGHENY PASSAGE - DRAVOS LANDING CAMP SITE","+40.289364,-79.777282",
      "GAP - GREAT ALLEGHENY PASSAGE - DUNCAN HOLLOW RUN","+40.291954,-79.799813",
      "GAP - GREAT ALLEGHENY PASSAGE - DUNCAN ST TRAIL ACCESS","+40.275039,-79.798218",
      "GAP - GREAT ALLEGHENY PASSAGE - ENTRANCE TO DEAD MAN'S HOLLOW","+40.317887,-79.840709",
      "GAP - GREAT ALLEGHENY PASSAGE - FELLABAUM ST TRAIL ACCESS","+40.271404,-79.796416",
      "GAP - GREAT ALLEGHENY PASSAGE - FINNEY RUN WOODEN BRIDGE","+40.247127,-79.801524",
      "GAP - GREAT ALLEGHENY PASSAGE - HARPER ST TRAIL ACCESS","+40.314275,-79.822673",
      "GAP - GREAT ALLEGHENY PASSAGE - HARPERS POND","+40.318138,-79.814708",
      "GAP - GREAT ALLEGHENY PASSAGE - JENNINGS RUN WOODEN BRIDGE","+40.259649,-79.791234",
      "GAP - GREAT ALLEGHENY PASSAGE - LIBERTY TRAIL ACCESS","+40.323303,-79.847992",
      "GAP - GREAT ALLEGHENY PASSAGE - LINCOLN/ELIZABETH TWP BORDER","+40.314431,-79.838177",
      "GAP - GREAT ALLEGHENY PASSAGE - LINCOLN/LIBERTY BORDER","+40.319974,-79.841952",
      "GAP - GREAT ALLEGHENY PASSAGE - LOCUST GROVE RD TRAIL ACCESS","+40.316206,-79.795033",
      "GAP - GREAT ALLEGHENY PASSAGE - MARIA ST TRAIL ACCESS","+40.266013,-79.791679",
      "GAP - GREAT ALLEGHENY PASSAGE - MCCORKLE CT TRAIL ACCESS","+40.272231,-79.797176",
      "GAP - GREAT ALLEGHENY PASSAGE - MCGOWAN ST TRAIL ACCESS","+40.279778,-79.797804",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 116","+40.227116,-79.783995",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 117","+40.230137,-79.802433",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 118","+40.242439,-79.805804",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 119","+40.254860,-79.796466",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 120","+40.267360,-79.792861",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 121","+40.280542,-79.797387",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 122","+40.283590,-79.779644",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 123","+40.295088,-79.785299",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 124","+40.291996,-79.800245",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 125","+40.303854,-79.806346",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 126","+40.312430,-79.792257",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 127","+40.318774,-79.807416",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 128","+40.313271,-79.824029",
      "GAP - GREAT ALLEGHENY PASSAGE - MILE POST 129","+40.315213,-79.838706",
      "GAP - GREAT ALLEGHENY PASSAGE - MILESTONE PAVILLION","+40.279469,-79.797601",
      "GAP - GREAT ALLEGHENY PASSAGE - OCEAN ST TRAIL ACCESS","+40.228697,-79.794584",
      "GAP - GREAT ALLEGHENY PASSAGE - PARKVUE ST TRAIL ACCESS","+40.246023,-79.802429",
      "GAP - GREAT ALLEGHENY PASSAGE - POLLOCK RUN","+40.227466,-79.788797",
      "GAP - GREAT ALLEGHENY PASSAGE - SANDY LN TRAIL ACCESS","+40.313960,-79.792568",
      "GAP - GREAT ALLEGHENY PASSAGE - SHAFFER RUN RD TRAIL ACCESS","+40.318103,-79.801279",
      "GAP - GREAT ALLEGHENY PASSAGE - SMITHDALE RD NORTH TRAIL ACCESS","+40.230697,-79.803911",
      "GAP - GREAT ALLEGHENY PASSAGE - SMITHDALE RD SOUTH TRAIL ACCESS","+40.228960,-79.796995",
      "GAP - GREAT ALLEGHENY PASSAGE - ST. DAVID DR TRAIL ACCESS","+40.317218,-79.818054",
      "GAP - GREAT ALLEGHENY PASSAGE - STONER ST TRAIL ACCESS","+40.317971,-79.800583",
      "GAP - GREAT ALLEGHENY PASSAGE - SUTERSVILLE TRAIL ACCESS","+40.241850,-79.806130",
      "GAP - GREAT ALLEGHENY PASSAGE - TERZA ST TRAIL ACCESS","+40.316701,-79.796199",
      "GAP - GREAT ALLEGHENY PASSAGE - THE RED WATERFALL","+40.256297,-79.794874",
      "GAP - GREAT ALLEGHENY PASSAGE - THE SHORES OF TRIPOLI","+40.318751,-79.806294",
      "GAP - GREAT ALLEGHENY PASSAGE - TWELE BALLFIELD TRAIL ACCESS","+40.312347,-79.792571",
      "GAP - GREAT ALLEGHENY PASSAGE - TWELE RD NORTH TRAIL ACCESS","+40.313234,-79.792370",
      "GAP - GREAT ALLEGHENY PASSAGE - TWELE RD SOUTH TRAIL ACCESS","+40.310929,-79.792552",
      "GAP - GREAT ALLEGHENY PASSAGE - VERIZON UNDERGROUND UTILITIES","+40.295298,-79.805612",
      "GAP - GREAT ALLEGHENY PASSAGE - WALTER ST TRAIL ACCESS","+40.312950,-79.824411",
      "GAP - GREAT ALLEGHENY PASSAGE - WEST PENN POWER TRANSMISSION","+40.295012,-79.785242",
      "GAP - GREAT ALLEGHENY PASSAGE - WHITE WATERFALL","+40.282925,-79.787651",
      "GAP - GREAT ALLEGHENY PASSAGE - WIDE DR TRAIL ACCESS","+40.317268,-79.797839",

      "1 GREAT ALLEGHENY PASSAGE",            "+40.289319,-79.778245",
      "5 GREAT ALLEGHENY PASSAGE",            "+40.288679,-79.777228",
      "1 GREATER ALLEGHENY PASSAGE",          "+40.315213,-79.838706",
      "2 GREATER ALLEGHENY PASSAGE",          "+40.323303,-79.847992",
      "3 GREATER ALLEGHENY PASSAGE",          "+40.319974,-79.841952",
      "4 GREATER ALLEGHENY PASSAGE",          "+40.313271,-79.824029",
      "5 GREATER ALLEGHENY PASSAGE",          "+40.317887,-79.840709",
      "6 GREATER ALLEGHENY PASSAGE",          "+40.314431,-79.838177",
      "7 GREATER ALLEGHENY PASSAGE",          "+40.310436,-79.830908",
      "9 GREATER ALLEGHENY PASSAGE",          "+40.318774,-79.807416",
      "10 GREATER ALLEGHENY PASSAGE",         "+40.312319,-79.825205",
      "11 GREATER ALLEGHENY PASSAGE",         "+40.312950,-79.824411",
      "13 GREATER ALLEGHENY PASSAGE",         "+40.314275,-79.822673",
      "14 GREATER ALLEGHENY PASSAGE",         "+40.317218,-79.818054",
      "16 GREATER ALLEGHENY PASSAGE",         "+40.318138,-79.814708",
      "17 GREATER ALLEGHENY PASSAGE",         "+40.312430,-79.792257",
      "18 GREATER ALLEGHENY PASSAGE",         "+40.318809,-79.811013",
      "19 GREATER ALLEGHENY PASSAGE",         "+40.318751,-79.806294",
      "20 GREATER ALLEGHENY PASSAGE",         "+40.318103,-79.801279",
      "21 GREATER ALLEGHENY PASSAGE",         "+40.317971,-79.800583",
      "22 GREATER ALLEGHENY PASSAGE",         "+40.317268,-79.797839",
      "23 GREATER ALLEGHENY PASSAGE",         "+40.316701,-79.796199",
      "24 GREATER ALLEGHENY PASSAGE",         "+40.316206,-79.795033",
      "26 GREATER ALLEGHENY PASSAGE",         "+40.303854,-79.806346",
      "27 GREATER ALLEGHENY PASSAGE",         "+40.315528,-79.793851",
      "28 GREATER ALLEGHENY PASSAGE",         "+40.313960,-79.792568",
      "29 GREATER ALLEGHENY PASSAGE",         "+40.313234,-79.792370",
      "30 GREATER ALLEGHENY PASSAGE",         "+40.312347,-79.792571",
      "31 GREATER ALLEGHENY PASSAGE",         "+40.310929,-79.792552",
      "32 GREATER ALLEGHENY PASSAGE",         "+40.309757,-79.793342",
      "33 GREATER ALLEGHENY PASSAGE",         "+40.291996,-79.800245",
      "34 GREATER ALLEGHENY PASSAGE",         "+40.303700,-79.806355",
      "35 GREATER ALLEGHENY PASSAGE",         "+40.295298,-79.805612",
      "36 GREATER ALLEGHENY PASSAGE",         "+40.295088,-79.785299",
      "37 GREATER ALLEGHENY PASSAGE",         "+40.291954,-79.799813",
      "38 GREATER ALLEGHENY PASSAGE",         "+40.293859,-79.794353",
      "39 GREATER ALLEGHENY PASSAGE",         "+40.283590,-79.779644",
      "40 GREATER ALLEGHENY PASSAGE",         "+40.295012,-79.785242",
      "41 GREATER ALLEGHENY PASSAGE",         "+40.289381,-79.778269",
      "42 GREATER ALLEGHENY PASSAGE",         "+40.288723,-79.777233",
      "44 GREATER ALLEGHENY PASSAGE",         "+40.280542,-79.797387",
      "45 GREATER ALLEGHENY PASSAGE",         "+40.282925,-79.787651",
      "46 GREATER ALLEGHENY PASSAGE",         "+40.267360,-79.792861",
      "47 GREATER ALLEGHENY PASSAGE",         "+40.279778,-79.797804",
      "48 GREATER ALLEGHENY PASSAGE",         "+40.279469,-79.797601",
      "49 GREATER ALLEGHENY PASSAGE",         "+40.277242,-79.798170",
      "50 GREATER ALLEGHENY PASSAGE",         "+40.275039,-79.798218",
      "52 GREATER ALLEGHENY PASSAGE",         "+40.272231,-79.797176",
      "53 GREATER ALLEGHENY PASSAGE",         "+40.271404,-79.796416",
      "54 GREATER ALLEGHENY PASSAGE",         "+40.254860,-79.796466",
      "55 GREATER ALLEGHENY PASSAGE",         "+40.266013,-79.791679",
      "57 GREATER ALLEGHENY PASSAGE",         "+40.260067,-79.791001",
      "58 GREATER ALLEGHENY PASSAGE",         "+40.259649,-79.791234",
      "59 GREATER ALLEGHENY PASSAGE",         "+40.242439,-79.805804",
      "60 GREATER ALLEGHENY PASSAGE",         "+40.256297,-79.794874",
      "61 GREATER ALLEGHENY PASSAGE",         "+40.254238,-79.797022",
      "62 GREATER ALLEGHENY PASSAGE",         "+40.252348,-79.798568",
      "65 GREATER ALLEGHENY PASSAGE",         "+40.247127,-79.801524",
      "66 GREATER ALLEGHENY PASSAGE",         "+40.246023,-79.802429",
      "67 GREATER ALLEGHENY PASSAGE",         "+40.230137,-79.802433",
      "69 GREATER ALLEGHENY PASSAGE",         "+40.241850,-79.806130",
      "70 GREATER ALLEGHENY PASSAGE",         "+40.227116,-79.783995",
      "71 GREATER ALLEGHENY PASSAGE",         "+40.230697,-79.803911",
      "72 GREATER ALLEGHENY PASSAGE",         "+40.228960,-79.796995",
      "73 GREATER ALLEGHENY PASSAGE",         "+40.228697,-79.794584",
      "74 GREATER ALLEGHENY PASSAGE",         "+40.227466,-79.788797",
      "76 GREATER ALLEGHENY PASSAGE",         "+40.227078,-79.783262"
  });
  
  static final Properties PLACE_GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "EFSD CENTRAL ELEMENTARY SCHOOL",                 "+40.276289,-79.825784",
      "ESFD ELIZABETH FORWARED FOOTBALL STADIUM",       "+40.255110,-79.854082",
      "EFSD ELIZABETH FORWARD SCHOOL DISCTIT OFFICES",  "+40.275073,-79.826080",
      "EFSD ELIZABETH FORWARD MIDDLE SCHOOL",           "+40.274391,-79.825838",
      "EFSD ELIZABETH FORWARD WARRIOR STADIUM",         "+40.255110,-79.854082",

      "VIRGINIA MANNOR APTS - BLDG 1",        "+40.317093,-79.808133",
      "VIRGINIA MANNOR APTS - BLDG 2",        "+40.317285,-79.807641",
      "VIRGINIA MANNOR APTS - BLDG 3",        "+40.317307,-79.807360",
      "VIRGINIA MANNOR APTS - BLDG 4",        "+40.317285,-79.807641",
      "VIRGINIA MANNOR APTS - BLDG 5",        "+40.317539,-79.809391",
      "VIRGINIA MANNOR APTS - BLDG 6",        "+40.317285,-79.807641"
  });
}
