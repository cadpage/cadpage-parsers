package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchArchonixParser;



public class NJCamdenCountyAParser extends DispatchArchonixParser {

  public NJCamdenCountyAParser() {
    super(CITY_CODES, null, "CAMDEN COUNTY", "NJ", ARCH_FLG_OPT_CITY);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setupSaintNames("JOHN", "MARK", "MORITZ");
  }

  @Override
  public String getFilter() {
    return "cccademail@camdencounty.com,cccalert@camdencodps.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Event Notification")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      subject = body.substring(0,pt).trim();
      body = body.substring(pt+1).trim();
    }
    if (!super.parseMsg(subject, body, data)) return false;
    String place = data.strPlace;
    if (place.startsWith("EXIT")) {
      int pt = place.indexOf(" - ");
      if (pt >= 0) place = place.substring(0,pt).trim();
      data.strAddress = append(data.strAddress, " ", place);
      data.strPlace = "";
    }
    data.strAddress = data.strAddress.replace("MICKLE BD", "DR MARTIN LUTHER KING BLVD");
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

  private static final Pattern TRAIL_BOUND_PTN = Pattern.compile(" +[NSEW]B$");

  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = TRAIL_BOUND_PTN.matcher(address).replaceFirst("");
    return super.adjustGpsLookupAddress(address);
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "00 ACE",                               "+39.362116,-74.440608",
      "01 ACE",                               "+39.362995,-74.442863",
      "02 ACE",                               "+39.363511,-74.444765",
      "03 ACE",                               "+39.364010,-74.446629",
      "04 ACE",                               "+39.364700,-74.448171",
      "05 ACE",                               "+39.365189,-74.449962",
      "06 ACE",                               "+39.365609,-74.451537",
      "07 ACE",                               "+39.365948,-74.452757",
      "08 ACE",                               "+39.366419,-74.454417",
      "09 ACE",                               "+39.366793,-74.455816",
      "10 ACE",                               "+39.367175,-74.457618",
      "20 ACE",                               "+39.373824,-74.474089",
      "30 ACE",                               "+39.380846,-74.490410",
      "40 ACE",                               "+39.387678,-74.506911",
      "50 ACE",                               "+39.398776,-74.517558",
      "60 ACE",                               "+39.405958,-74.533895",
      "70 ACE",                               "+39.412127,-74.549652",
      "80 ACE",                               "+39.419706,-74.565655",
      "90 ACE",                               "+39.427957,-74.580887",
      "100 ACE",                              "+39.437833,-74.594314",
      "110 ACE",                              "+39.443474,-74.611445",
      "120 ACE",                              "+39.451760,-74.626283",
      "130 ACE",                              "+39.465315,-74.631475",
      "140 ACE",                              "+39.478960,-74.637682",
      "150 ACE",                              "+39.488974,-74.651244",
      "160 ACE",                              "+39.498714,-74.665030",
      "170 ACE",                              "+39.508404,-74.678959",
      "180 ACE",                              "+39.518284,-74.692754",
      "190 ACE",                              "+39.528425,-74.706091",
      "200 ACE",                              "+39.539236,-74.718433",
      "210 ACE",                              "+39.549775,-74.731317",
      "220 ACE",                              "+39.558072,-74.746963",
      "230 ACE",                              "+39.568542,-74.759928",
      "240 ACE",                              "+39.577879,-74.774135",
      "250 ACE",                              "+39.587303,-74.788467",
      "260 ACE",                              "+39.596756,-74.802647",
      "270 ACE",                              "+39.606351,-74.816541",
      "280 ACE",                              "+39.615951,-74.830714",
      "290 ACE",                              "+39.625612,-74.844793",
      "300 ACE",                              "+39.635705,-74.858039",
      "310 ACE",                              "+39.646977,-74.869725",
      "320 ACE",                              "+39.658070,-74.881849",
      "330 ACE",                              "+39.669361,-74.893790",
      "340 ACE",                              "+39.680905,-74.905087",
      "350 ACE",                              "+39.690325,-74.919317",
      "360 ACE",                              "+39.697226,-74.935907",
      "370 ACE",                              "+39.702749,-74.953124",
      "380 ACE",                              "+39.706522,-74.971290",
      "390 ACE",                              "+39.714082,-74.986828",
      "400 ACE",                              "+39.724507,-74.999624",
      "410 ACE",                              "+39.736107,-75.010921",
      "420 ACE",                              "+39.747572,-75.022512",
      "430 ACE",                              "+39.756749,-75.037127",
      "440 ACE",                              "+39.768739,-75.048234",
      "450 ACE",                              "+39.393971,-74.511197",
      "460 ACE",                              "+39.395198,-74.512194",
      "470 ACE",                              "+39.396313,-74.513403",
      "480 ACE",                              "+39.397068,-74.514453",
      "490 ACE",                              "+39.397950,-74.516000",
      "510 ACE",                              "+39.399721,-74.519373",
      "520 ACE",                              "+39.400323,-74.520463",
      "530 ACE",                              "+39.401176,-74.522127",
      "540 ACE",                              "+39.401992,-74.523661",
      "550 ACE",                              "+39.402740,-74.525070",
      "560 ACE",                              "+39.403526,-74.526567",
      "570 ACE",                              "+39.404439,-74.528240",
      "580 ACE",                              "+39.405232,-74.529841",
      "590 ACE",                              "+39.405782,-74.531700",
      "610 ACE",                              "+39.405919,-74.535572",
      "620 ACE",                              "+39.405974,-74.537682",
      "630 ACE",                              "+39.406290,-74.539307",
      "640 ACE",                              "+39.406914,-74.540935",
      "650 ACE",                              "+39.407378,-74.541751",
      "660 ACE",                              "+39.408352,-74.543320",
      "670 ACE",                              "+39.409197,-74.544684",
      "680 ACE",                              "+39.409993,-74.545999",
      "690 ACE",                              "+39.410936,-74.547521",
      "710 ACE",                              "+39.412834,-74.551002",
      "720 ACE",                              "+39.413639,-74.552719",
      "730 ACE",                              "+39.414496,-74.554557",
      "740 ACE",                              "+39.415090,-74.555806",
      "750 ACE",                              "+39.415976,-74.557686",
      "760 ACE",                              "+39.416862,-74.559584",
      "770 ACE",                              "+39.417403,-74.560728",
      "780 ACE",                              "+39.418011,-74.562051",
      "790 ACE",                              "+39.418962,-74.564073",
      "810 ACE",                              "+39.420474,-74.567272",
      "820 ACE",                              "+39.421213,-74.568870",
      "830 ACE",                              "+39.421968,-74.570476",
      "840 ACE",                              "+39.422736,-74.572099",
      "850 ACE",                              "+39.423469,-74.573674",
      "860 ACE",                              "+39.424269,-74.575356",
      "870 ACE",                              "+39.425006,-74.576841",
      "880 ACE",                              "+39.425881,-74.578288",
      "890 ACE",                              "+39.426850,-74.579625",
      "910 ACE",                              "+39.429053,-74.581948",
      "920 ACE",                              "+39.430193,-74.583019",
      "930 ACE",                              "+39.431376,-74.584120",
      "940 ACE",                              "+39.432548,-74.585246",
      "950 ACE",                              "+39.433848,-74.586680",
      "960 ACE",                              "+39.434703,-74.587721",
      "970 ACE",                              "+39.435687,-74.589338",
      "980 ACE",                              "+39.436464,-74.590863",
      "990 ACE",                              "+39.437332,-74.592836",
      "1010 ACE",                             "+39.438379,-74.595984",
      "1020 ACE",                             "+39.438941,-74.597685",
      "1030 ACE",                             "+39.439530,-74.599413",
      "1040 ACE",                             "+39.440081,-74.601142",
      "1050 ACE",                             "+39.440644,-74.602861",
      "1060 ACE",                             "+39.441217,-74.604594",
      "1070 ACE",                             "+39.441774,-74.606281",
      "1080 ACE",                             "+39.442365,-74.608068",
      "1090 ACE",                             "+39.442926,-74.609764",
      "1110 ACE",                             "+39.444077,-74.613285",
      "1120 ACE",                             "+39.444634,-74.614983",
      "1130 ACE",                             "+39.445199,-74.616652",
      "1140 ACE",                             "+39.445668,-74.617704",
      "1150 ACE",                             "+39.446341,-74.619111",
      "1160 ACE",                             "+39.447527,-74.621237",
      "1170 ACE",                             "+39.448495,-74.622663",
      "1180 ACE",                             "+39.449485,-74.623926",
      "1190 ACE",                             "+39.450578,-74.625141",
      "1210 ACE",                             "+39.452989,-74.627310",
      "1220 ACE",                             "+39.454247,-74.628192",
      "1230 ACE",                             "+39.455509,-74.628957",
      "1240 ACE",                             "+39.456870,-74.629618",
      "1250 ACE",                             "+39.458245,-74.630054",
      "1260 ACE",                             "+39.459692,-74.630586",
      "1270 ACE",                             "+39.461051,-74.630867",
      "1280 ACE",                             "+39.462499,-74.631075",
      "1290 ACE",                             "+39.463961,-74.631282",
      "1310 ACE",                             "+39.466751,-74.631672",
      "1320 ACE",                             "+39.468196,-74.631881",
      "1330 ACE",                             "+39.469660,-74.632173",
      "1340 ACE",                             "+39.471056,-74.632573",
      "1350 ACE",                             "+39.472511,-74.633136",
      "1360 ACE",                             "+39.473893,-74.633796",
      "1370 ACE",                             "+39.475138,-74.634515",
      "1380 ACE",                             "+39.476584,-74.635574",
      "1390 ACE",                             "+39.477660,-74.636395",
      "1410 ACE",                             "+39.480022,-74.638876",
      "1420 ACE",                             "+39.481082,-74.640163",
      "1430 ACE",                             "+39.482109,-74.641530",
      "1440 ACE",                             "+39.483082,-74.642907",
      "1450 ACE",                             "+39.484062,-74.644292",
      "1460 ACE",                             "+39.485050,-74.645687",
      "1470 ACE",                             "+39.486006,-74.647041",
      "1480 ACE",                             "+39.486995,-74.648441",
      "1490 ACE",                             "+39.487968,-74.649809",
      "1510 ACE",                             "+39.489927,-74.652591",
      "1520 ACE",                             "+39.490918,-74.653994",
      "1530 ACE",                             "+39.491875,-74.655356",
      "1540 ACE",                             "+39.492867,-74.656754",
      "1550 ACE",                             "+39.493834,-74.658123",
      "1560 ACE",                             "+39.494456,-74.659945",
      "1570 ACE",                             "+39.495423,-74.661311",
      "1580 ACE",                             "+39.496421,-74.662723",
      "1590 ACE",                             "+39.497741,-74.663650",
      "1610 ACE",                             "+39.499702,-74.666433",
      "1620 ACE",                             "+39.500682,-74.667814",
      "1630 ACE",                             "+39.501684,-74.669234",
      "1640 ACE",                             "+39.502646,-74.670549",
      "1650 ACE",                             "+39.503597,-74.671955",
      "1660 ACE",                             "+39.504582,-74.673388",
      "1670 ACE",                             "+39.505513,-74.674757",
      "1680 ACE",                             "+39.506465,-74.676145",
      "1690 ACE",                             "+39.507441,-74.677573",
      "1710 ACE",                             "+39.509605,-74.680471",
      "1720 ACE",                             "+39.510354,-74.681489",
      "1730 ACE",                             "+39.511502,-74.683148",
      "1740 ACE",                             "+39.512435,-74.684420",
      "1750 ACE",                             "+39.513249,-74.685462",
      "1760 ACE",                             "+39.514333,-74.686991",
      "1770 ACE",                             "+39.515194,-74.688324",
      "1780 ACE",                             "+39.516322,-74.689971",
      "1790 ACE",                             "+39.517293,-74.691346",
      "1810 ACE",                             "+39.519193,-74.694046",
      "1820 ACE",                             "+39.520172,-74.695440",
      "1830 ACE",                             "+39.521165,-74.696842",
      "1840 ACE",                             "+39.522204,-74.698311",
      "1850 ACE",                             "+39.523111,-74.699595",
      "1860 ACE",                             "+39.524126,-74.700961",
      "1870 ACE",                             "+39.525144,-74.702263",
      "1880 ACE",                             "+39.526194,-74.703528",
      "1890 ACE",                             "+39.527281,-74.704787",
      "1910 ACE",                             "+39.529443,-74.707251",
      "1920 ACE",                             "+39.530513,-74.708470",
      "1930 ACE",                             "+39.531623,-74.709736",
      "1940 ACE",                             "+39.532699,-74.710970",
      "1950 ACE",                             "+39.533790,-74.712219",
      "1960 ACE",                             "+39.534884,-74.713463",
      "1970 ACE",                             "+39.535856,-74.714574",
      "1980 ACE",                             "+39.537058,-74.715947",
      "1990 ACE",                             "+39.538147,-74.717189",
      "2010 ACE",                             "+39.540329,-74.719679",
      "2020 ACE",                             "+39.541430,-74.720935",
      "2030 ACE",                             "+39.542503,-74.722160",
      "2040 ACE",                             "+39.543582,-74.723390",
      "2050 ACE",                             "+39.544677,-74.724641",
      "2060 ACE",                             "+39.545761,-74.725891",
      "2070 ACE",                             "+39.546811,-74.727172",
      "2080 ACE",                             "+39.547751,-74.728397",
      "2090 ACE",                             "+39.548821,-74.729894",
      "2110 ACE",                             "+39.550681,-74.732703",
      "2120 ACE",                             "+39.551622,-74.734142",
      "2130 ACE",                             "+39.552562,-74.735572",
      "2140 ACE",                             "+39.553493,-74.736992",
      "2150 ACE",                             "+39.554449,-74.738444",
      "2160 ACE",                             "+39.555389,-74.739876",
      "2170 ACE",                             "+39.556325,-74.741308",
      "2180 ACE",                             "+39.557275,-74.742752",
      "2190 ACE",                             "+39.558191,-74.744148",
      "2210 ACE",                             "+39.560104,-74.747070",
      "2220 ACE",                             "+39.561047,-74.748510",
      "2230 ACE",                             "+39.561990,-74.749940",
      "2240 ACE",                             "+39.562901,-74.751325",
      "2250 ACE",                             "+39.563897,-74.752845",
      "2260 ACE",                             "+39.564772,-74.754185",
      "2270 ACE",                             "+39.565717,-74.755625",
      "2280 ACE",                             "+39.566660,-74.757064",
      "2290 ACE",                             "+39.567626,-74.758498",
      "2310 ACE",                             "+39.569416,-74.761269",
      "2320 ACE",                             "+39.570367,-74.762677",
      "2330 ACE",                             "+39.571298,-74.764128",
      "2340 ACE",                             "+39.572238,-74.765568",
      "2350 ACE",                             "+39.573179,-74.766999",
      "2360 ACE",                             "+39.574121,-74.768434",
      "2370 ACE",                             "+39.574685,-74.770270",
      "2380 ACE",                             "+39.576005,-74.771303",
      "2390 ACE",                             "+39.576937,-74.772732",
      "2410 ACE",                             "+39.578803,-74.775591",
      "2420 ACE",                             "+39.579751,-74.777067",
      "2430 ACE",                             "+39.580652,-74.778472",
      "2440 ACE",                             "+39.581587,-74.779936",
      "2450 ACE",                             "+39.582550,-74.781444",
      "2460 ACE",                             "+39.583440,-74.782829",
      "2470 ACE",                             "+39.584398,-74.784207",
      "2480 ACE",                             "+39.585409,-74.785631",
      "2490 ACE",                             "+39.586346,-74.787013",
      "2510 ACE",                             "+39.588192,-74.789802",
      "2520 ACE",                             "+39.589146,-74.791243",
      "2530 ACE",                             "+39.590072,-74.792663",
      "2540 ACE",                             "+39.591042,-74.794121",
      "2550 ACE",                             "+39.591993,-74.795570",
      "2560 ACE",                             "+39.592637,-74.797340",
      "2570 ACE",                             "+39.593879,-74.798416",
      "2580 ACE",                             "+39.594891,-74.799941",
      "2590 ACE",                             "+39.595756,-74.801250",
      "2610 ACE",                             "+39.597660,-74.803844",
      "2620 ACE",                             "+39.598653,-74.805239",
      "2630 ACE",                             "+39.599615,-74.806649",
      "2640 ACE",                             "+39.600573,-74.808049",
      "2650 ACE",                             "+39.601527,-74.809457",
      "2660 ACE",                             "+39.602496,-74.810872",
      "2670 ACE",                             "+39.603458,-74.812290",
      "2680 ACE",                             "+39.604419,-74.813705",
      "2690 ACE",                             "+39.605374,-74.815106",
      "2710 ACE",                             "+39.607299,-74.817928",
      "2720 ACE",                             "+39.608266,-74.819350",
      "2730 ACE",                             "+39.609260,-74.820764",
      "2740 ACE",                             "+39.610195,-74.822182",
      "2750 ACE",                             "+39.611171,-74.823629",
      "2760 ACE",                             "+39.612201,-74.825169",
      "2770 ACE",                             "+39.613209,-74.826617",
      "2780 ACE",                             "+39.614022,-74.827897",
      "2790 ACE",                             "+39.614980,-74.829306",
      "2810 ACE",                             "+39.616942,-74.831919",
      "2820 ACE",                             "+39.617746,-74.833060",
      "2830 ACE",                             "+39.618809,-74.834652",
      "2840 ACE",                             "+39.619859,-74.836230",
      "2850 ACE",                             "+39.620786,-74.837612",
      "2860 ACE",                             "+39.621768,-74.839067",
      "2870 ACE",                             "+39.622719,-74.840464",
      "2880 ACE",                             "+39.623663,-74.841888",
      "2890 ACE",                             "+39.624620,-74.843315",
      "2910 ACE",                             "+39.626536,-74.846140",
      "2920 ACE",                             "+39.627490,-74.847566",
      "2930 ACE",                             "+39.628368,-74.848882",
      "2940 ACE",                             "+39.629405,-74.850360",
      "2950 ACE",                             "+39.630389,-74.851725",
      "2960 ACE",                             "+39.631376,-74.853022",
      "2970 ACE",                             "+39.632443,-74.854366",
      "2980 ACE",                             "+39.633500,-74.855624",
      "2990 ACE",                             "+39.634585,-74.856844",
      "3010 ACE",                             "+39.636806,-74.859183",
      "3020 ACE",                             "+39.637932,-74.860356",
      "3030 ACE",                             "+39.639035,-74.861508",
      "3040 ACE",                             "+39.640165,-74.862679",
      "3050 ACE",                             "+39.641295,-74.863852",
      "3060 ACE",                             "+39.642436,-74.865042",
      "3070 ACE",                             "+39.643504,-74.866149",
      "3080 ACE",                             "+39.644685,-74.867384",
      "3090 ACE",                             "+39.645832,-74.868573",
      "3110 ACE",                             "+39.648103,-74.870892",
      "3120 ACE",                             "+39.649386,-74.872303",
      "3130 ACE",                             "+39.650149,-74.873638",
      "3140 ACE",                             "+39.651314,-74.874870",
      "3150 ACE",                             "+39.652432,-74.875966",
      "3160 ACE",                             "+39.653550,-74.877137",
      "3170 ACE",                             "+39.654684,-74.878315",
      "3180 ACE",                             "+39.655818,-74.879503",
      "3190 ACE",                             "+39.656954,-74.880684",
      "3210 ACE",                             "+39.659216,-74.883038",
      "3220 ACE",                             "+39.660339,-74.884209",
      "3230 ACE",                             "+39.661662,-74.885052",
      "3240 ACE",                             "+39.662607,-74.886575",
      "3250 ACE",                             "+39.663741,-74.887745",
      "3260 ACE",                             "+39.664873,-74.888928",
      "3270 ACE",                             "+39.665899,-74.890007",
      "3280 ACE",                             "+39.667130,-74.891285",
      "3290 ACE",                             "+39.668233,-74.892498",
      "3310 ACE",                             "+39.670263,-74.894729",
      "3320 ACE",                             "+39.671548,-74.896019",
      "3330 ACE",                             "+39.672688,-74.897205",
      "3340 ACE",                             "+39.673761,-74.898330",
      "3350 ACE",                             "+39.674950,-74.899565",
      "3360 ACE",                             "+39.676384,-74.900253",
      "3370 ACE",                             "+39.677532,-74.901447",
      "3380 ACE",                             "+39.678640,-74.902608",
      "3390 ACE",                             "+39.679767,-74.903794",
      "3410 ACE",                             "+39.681924,-74.906299",
      "3420 ACE",                             "+39.682960,-74.907597",
      "3430 ACE",                             "+39.683993,-74.908964",
      "3440 ACE",                             "+39.684986,-74.910354",
      "3450 ACE",                             "+39.685970,-74.911798",
      "3460 ACE",                             "+39.686904,-74.913245",
      "3470 ACE",                             "+39.687821,-74.914756",
      "3480 ACE",                             "+39.688708,-74.916281",
      "3490 ACE",                             "+39.689534,-74.917789",
      "3510 ACE",                             "+39.691043,-74.920804",
      "3520 ACE",                             "+39.691836,-74.922504",
      "3530 ACE",                             "+39.692549,-74.924159",
      "3540 ACE",                             "+39.693229,-74.925831",
      "3550 ACE",                             "+39.693895,-74.927464",
      "3560 ACE",                             "+39.694529,-74.929010",
      "3570 ACE",                             "+39.695270,-74.930846",
      "3580 ACE",                             "+39.695939,-74.932502",
      "3590 ACE",                             "+39.696600,-74.934194",
      "3610 ACE",                             "+39.697820,-74.937562",
      "3620 ACE",                             "+39.698419,-74.939230",
      "3630 ACE",                             "+39.699038,-74.940974",
      "3640 ACE",                             "+39.699639,-74.942663",
      "3650 ACE",                             "+39.700232,-74.944353",
      "3660 ACE",                             "+39.700807,-74.946102",
      "3670 ACE",                             "+39.701346,-74.947868",
      "3680 ACE",                             "+39.701848,-74.949610",
      "3690 ACE",                             "+39.702357,-74.951482",
      "3710 ACE",                             "+39.703177,-74.955012",
      "3720 ACE",                             "+39.703555,-74.956831",
      "3730 ACE",                             "+39.703928,-74.958636",
      "3740 ACE",                             "+39.704302,-74.960444",
      "3750 ACE",                             "+39.704675,-74.962277",
      "3760 ACE",                             "+39.705030,-74.964011",
      "3770 ACE",                             "+39.705422,-74.965917",
      "3780 ACE",                             "+39.705768,-74.967613",
      "3790 ACE",                             "+39.706168,-74.969559",
      "3810 ACE",                             "+39.706817,-74.972697",
      "3820 ACE",                             "+39.707396,-74.974528",
      "3830 ACE",                             "+39.708032,-74.976399",
      "3840 ACE",                             "+39.708725,-74.978022",
      "3850 ACE",                             "+39.709455,-74.979589",
      "3860 ACE",                             "+39.710331,-74.981084",
      "3870 ACE",                             "+39.711331,-74.982570",
      "3880 ACE",                             "+39.712192,-74.983933",
      "3890 ACE",                             "+39.713119,-74.985373",
      "3910 ACE",                             "+39.714969,-74.988217",
      "3920 ACE",                             "+39.715917,-74.989683",
      "3930 ACE",                             "+39.716821,-74.991013",
      "3940 ACE",                             "+39.717837,-74.992383",
      "3950 ACE",                             "+39.718857,-74.993697",
      "3960 ACE",                             "+39.719933,-74.994963",
      "3970 ACE",                             "+39.721064,-74.996206",
      "3980 ACE",                             "+39.722181,-74.997343",
      "3990 ACE",                             "+39.723334,-74.998476",
      "4010 ACE",                             "+39.725637,-75.000709",
      "4020 ACE",                             "+39.726816,-75.001874",
      "4030 ACE",                             "+39.727997,-75.003035",
      "4040 ACE",                             "+39.729176,-75.004210",
      "4050 ACE",                             "+39.730289,-75.005319",
      "4060 ACE",                             "+39.731404,-75.006471",
      "4070 ACE",                             "+39.732562,-75.007638",
      "4080 ACE",                             "+39.733715,-75.008755",
      "4090 ACE",                             "+39.734908,-75.009864",
      "4110 ACE",                             "+39.737274,-75.012055",
      "4120 ACE",                             "+39.738414,-75.013220",
      "4130 ACE",                             "+39.739576,-75.014357",
      "4140 ACE",                             "+39.740442,-75.016000",
      "4150 ACE",                             "+39.741901,-75.016628",
      "4160 ACE",                             "+39.743084,-75.017787",
      "4170 ACE",                             "+39.744232,-75.018911",
      "4180 ACE",                             "+39.745315,-75.020002",
      "4190 ACE",                             "+39.746498,-75.021280",
      "4210 ACE",                             "+39.748632,-75.023810",
      "4220 ACE",                             "+39.749642,-75.025123",
      "4230 ACE",                             "+39.750645,-75.026502",
      "4240 ACE",                             "+39.751832,-75.028246",
      "4250 ACE",                             "+39.752599,-75.029456",
      "4260 ACE",                             "+39.753437,-75.030851",
      "4270 ACE",                             "+39.754347,-75.032480",
      "4280 ACE",                             "+39.755108,-75.033919",
      "4290 ACE",                             "+39.755980,-75.035603",
      "4310 ACE",                             "+39.757564,-75.038625",
      "4320 ACE",                             "+39.758529,-75.039975",
      "4330 ACE",                             "+39.759619,-75.041147",
      "4340 ACE",                             "+39.760861,-75.042119",
      "4350 ACE",                             "+39.762133,-75.042942",
      "4360 ACE",                             "+39.763444,-75.043773",
      "4370 ACE",                             "+39.764755,-75.044729",
      "4380 ACE",                             "+39.765997,-75.045759",
      "4390 ACE",                             "+39.767126,-75.046819",

      "00 ACE CN",                            "+39.363258,-74.442814",
      "01 ACE CN",                            "+39.360980,-74.440570",
      "02 ACE CN",                            "+39.361832,-74.441989",
      "03 ACE CN",                            "+39.362287,-74.443841",
      "04 ACE CN",                            "+39.363369,-74.444539",
      "05 ACE CN",                            "+39.365065,-74.444040",
      "06 ACE CN",                            "+39.366374,-74.444294",
      "07 ACE CN",                            "+39.367814,-74.444296",
      "08 ACE CN",                            "+39.369197,-74.444880",
      "13 ACE CN",                            "+39.374466,-74.441088",
      "14 ACE CN",                            "+39.374012,-74.439306",
      "16 ACE CN",                            "+39.373827,-74.435647",
      "18 ACE CN",                            "+39.375511,-74.432636",
      "19 ACE CN",                            "+39.376855,-74.432010",
      "20 ACE CN",                            "+39.378065,-74.431000",
      "21 ACE CN",                            "+39.379217,-74.429879",
      "22 ACE CN",                            "+39.380489,-74.429039",

      "1 WATER ST",                           "+39.948970,-75.130830",

      "1201 YORKSHIP SQ N",                   "+39.906624,-75.106103",

      "BEN FRANKLIN BRIDGE PLAZA",            "+39.948700,-75.118600",
      "CAMDEN HIGH FOOTBALL FIELD",           "+39.930750,-75.093040",
      "PATCO BROADWAY STATION",               "+39.948970,-75.130830",

      "ROUTE 676 EXIT 1",                     "+39.902920,-75.111580",
      "ROUTE 676 EXIT 1C",                    "+39.904880,-75.113620",
      "ROUTE 676 EXIT 3",                     "+39.915400,-75.006410",
      "ROUTE 676 EXIT 4",                     "+39.926820,-75.115900",
      "ROUTE 676 EXIT 5A",                    "+39.943020,-75.113370"

  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "00",     "",
      "01",    "Audubon",
      "02",    "Audubon Park",
      "03",    "Barrington",
      "04",    "Bellmawr",
      "05",    "Berlin",
      "06",    "Berlin Twp",
      "07",    "Brooklawn",
      "08",    "Camden City",
      "09",    "Chesilhurst",
      "10",    "Clementon",
      "11",    "Collingswood",
      "12",    "Cherry Hill",
      "13",    "Gibbsboro",
      "14",    "Gloucester City",
      "15",    "Gloucester Twp",
      "16",    "Haddon Twp",
      "17",    "Haddonfield",
      "18",    "Haddon Heights",
      "19",    "Hi-Nella",
      "20",    "Laurel Springs",
      "21",    "Lawnside",
      "22",    "Lindenwold",
      "23",    "Magnolia",
      "24",    "Merchantville",
      "25",    "Mount Ephraim",
      "26",    "Oaklyn",
      "27",    "Pennsauken Twp",
      "28",    "Pine Hill",
      "29",    "Pine Valley",
      "30",    "Runnemede",
      "31",    "Somerdale",
      "32",    "Stratford",
      "33",    "Tavistock",
      "34",    "Voorhees Twp",
      "35",    "Waterford Twp",
      "36",    "Winslow Twp",
      "37",    "Woodlyne",
      "44",    "",
      "61",    "Atlantic City",
      "62",    "",
      "63",    "",
      "66",    "Cumberland County"
  });

}
