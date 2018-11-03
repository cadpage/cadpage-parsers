package net.anei.cadpage.parsers.ZNZ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;


public class ZNZNewZealandParser extends SmartAddressParser {

  private static final Pattern SUBJECT_UNIT_PTN = Pattern.compile("[ ,A-Z0-9]+");
  private static final Pattern POR_FIRE_PTN = Pattern.compile(" +POR[A-Z0-9]+FIRE$");
  
  private static final Pattern WRAP_BRK_PTN = Pattern.compile("(#F\\d+)(?=\\()");
  private static final Pattern END_PAGE_BREAK = Pattern.compile("#F\\d+(?=\n)");
  private static final Pattern TRAIL_SRC_PTN = Pattern.compile(" +- ([A-Z]+)$");
  private static final Pattern TRAIL_TIME_PTN = Pattern.compile(" +(\\d\\d:\\d\\d)$");
  
  private static final Pattern UNIT_PTN = Pattern.compile("\\(?([A-Z0-9, ]+)([\\)\\.]) *");
  private static final Pattern CODE_PTN1 = Pattern.compile("(?:([^-.]+?)[- ]+?)?([A-Z0-9/]+-[A-Z0-9]+) +");
  private static final Pattern CODE_PTN2 = Pattern.compile("(?:([- /A-Z0-9]+) - )?((?!AREA)[A-Z]{3,6}\\d?|SPRNKLR|FIRETEST) +");
  private static final Pattern LEAD_UNIT_PTN = Pattern.compile("[A-Z]+\\d+");
  private static final Pattern ALARM_TYPE_PTN = Pattern.compile("\\((Alarm Type [-A-Z0-9/ ]+)\\) *");
  private static final Pattern BOX_PTN = Pattern.compile("\\(Box ([-A-Z0-9 &]+)\\) *");
  private static final Pattern AK_PTN = Pattern.compile("(AK\\d+[A-Z]? .*? > [A-Z]+\\)) +(?:AK\\d+[A-Z]? +)? *");
  private static final Pattern EXTRA_PTN = Pattern.compile("([- A-Z0-9:&]+)\\.\\.? *");
  private static final Pattern EXTRA_PTN2 = Pattern.compile("([- A-Z0-9:&]+)\\.\\.?(?=#)");
  private static final Pattern NEAR_OFF_PTN = Pattern.compile("((?:NEAR|OFF) [- A-Z0-9\\?]+)\\. *");
  private static final Pattern XSTR_PTN = Pattern.compile("\\(XStr *([-A-Z0-9/ ]*)\\) *");
  private static final Pattern DOT_DOT_PTN = Pattern.compile("\\.(.*)\\. *");
  private static final Pattern GPS_PTN = Pattern.compile("\\(x-?(\\d+) ?y-?(\\d+)\\) *");
  private static final Pattern ID_PTN = Pattern.compile("#(F\\d+(?: \\d+)?)$");
  
  private static final Pattern UNKNNNNN = Pattern.compile("\\bUNKN\\d{4}\\b");
  private static final Pattern DOUBLED_ADDRESS = Pattern.compile("(\\d+) .* (\\1\\b.*)");
  private static final Pattern PLACE_APT_PTN = Pattern.compile("\\d{1,5}[A-Z]?|[A-Z]");

  public ZNZNewZealandParser() {
    super("", "", CountryCode.NZ);
    removeWords("TE");
    setupCities(CITY_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupProtectedNames("NO 3", "BLUE LAKE TOP 10 HOLIDAY PARK");
    setFieldList("UNIT CODE BOX PLACE ADDR APT CITY INFO X CALL GPS ID TIME SRC");
  }
  
  @Override
  public String getFilter() {
    return "silv@vodafone.co.nz,pager@firehouse.co.nz,nzfs.sms@gmail.com,dasmail@fire.org.nz,michael.upton2@gmail.com";
  }

  @Override
  public String getLocName() {
    return "New Zealand";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean mixedMsgOrder() { return true; }
    };
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() > 0) {
      if (body.startsWith("-----------------") || body.length() == 0) {
        if (subject.length() < 2) return false;
        if (subject.charAt(0) != '(' && subject.charAt(1) == '(') {
          subject = subject.substring(1);
        }
        body = subject.trim();
        subject = "";
      } else {
        String[] subFlds = subject.split("\\|");
        subject = subFlds[subFlds.length-1].trim();
        if (SUBJECT_UNIT_PTN.matcher(subject).matches()) {
          body = '(' + subject + ") " + body;
        }
      }
    }
    
    int pt = body.indexOf("\n Please do not reply");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = POR_FIRE_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start());
    
    match = WRAP_BRK_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(match.end()) + body.substring(0,match.end());
    } else {
      match = END_PAGE_BREAK.matcher(body);
      if (match.find()) body = body.substring(0,match.end());
    }
    body = body.replace('\n', ' ');
    
    match = TRAIL_SRC_PTN.matcher(body);
    if (match.find()) {
      data.strSource = match.group(1);
      body = body.substring(0,match.start());
    }
    
    match = TRAIL_TIME_PTN.matcher(body);
    if (match.find()) {
      data.strTime = match.group(1);
      body = body.substring(0,match.start());
    }
    
    match = UNIT_PTN.matcher(body);
    if (match.lookingAt()) {
      String unit = match.group(1);
      if (unit != null) {
        unit = unit.trim();
        if (match.group(2).equals(".")) {
          data.strCall = unit;
        } else {
          data.strUnit = unit;
        }
      }
      body = body.substring(match.end());
    }
    
    match = CODE_PTN1.matcher(body);
    if (match.lookingAt()) {
      String call = getOptGroup(match.group(1));
      data.strCode = match.group(2).trim();
      body = body.substring(match.end());
  
      if (call.endsWith(" STN") || call.equals("STN")) {
        data.strCode = "STN " + data.strCode;
        int len = call.length()-4;
        if (len < 0) len = 0;
        call = call.substring(0,len).trim();
      }
      if (data.strUnit.length() == 0 && LEAD_UNIT_PTN.matcher(call).matches()) {
        data.strUnit = call;
      } else { 
        data.strCall = append(data.strCall, " / ", call);
      }
    }
    
    else if ((match = CODE_PTN2.matcher(body)).lookingAt()) {
      String call = match.group(1);
      if (call != null) data.strCall = append(data.strCall, " / ", call);
      data.strCode = match.group(2);
      body = body.substring(match.end());
    }
    
    else return false;
    
    match = ALARM_TYPE_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCall = append(data.strCall, " / ", match.group(1).trim());
      body = body.substring(match.end());
    }
    
    match = BOX_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strBox = match.group(1).trim();
      body = body.substring(match.end());
    }
    
    match = AK_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strPlace = match.group(1).trim();
      body = body.substring(match.end());
    }
    
    pt = body.indexOf('.');
    if (pt < 0) return false;
    String sAddr = body.substring(0,pt).trim();
    body = body.substring(pt+1).trim();
    sAddr = UNKNNNNN.matcher(sAddr).replaceAll("@");
    sAddr = sAddr.replace('\\', '@');
    StartType st = (data.strPlace.length() > 0 ? StartType.START_ADDR : StartType.START_PLACE);
    parseAddress(st, FLAG_CHECK_STATUS | FLAG_EMPTY_ADDR_OK | FLAG_ANCHOR_END, sAddr, data);
    
    // if no address found, see if the place not is really a city
    if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
    } 
    
    // They commonly specify double cities, we want the innermost city name
    if (data.strCity.length() > 0) {
      parseAddress(StartType.START_ADDR, FLAG_EMPTY_ADDR_OK | FLAG_ANCHOR_END, data.strAddress, data);
    }
    
    // If we did not find a city, try parsing a city from middle of string
    // followed by call description
    else {
      String addr = data.strAddress;
      data.strAddress = "";
      parseAddress(StartType.START_ADDR, addr, data);
      data.strCall = append(data.strCall, " / ", getLeft());
    }
    data.strAddress = stripFieldEnd(data.strAddress, " NULL");
    
    match = EXTRA_PTN.matcher(body);
    if (match.lookingAt()) {
      String tmp = match.group(1).trim();
      body = body.substring(match.end());
      
      if (tmp != null && !tmp.startsWith("CNR ")) {
        if (tmp.startsWith("RP:")) {
          data.strApt = append(data.strApt, "-", tmp.substring(3).trim());
        } else {
          data.strPlace = append(data.strPlace, " ", tmp.trim());
        }
      }
      
      match = EXTRA_PTN.matcher(body);
      if (match.lookingAt()) {
        data.strSupp = match.group(1).trim();
        body = body.substring(match.end());
      }
    }
    
    match = NEAR_OFF_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strPlace = append(data.strPlace, " ", match.group(1).trim());
      body = body.substring(match.end());
    }
    
    match = XSTR_PTN.matcher(body);
    if (match.lookingAt()) {
      String cross = match.group(1).trim();
      body = body.substring(match.end());
      cross = stripFieldEnd(cross, "/");
      data.strCross = cross;
    }
    
    match = DOT_DOT_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCall = append(data.strCall, " / ", match.group(1).trim());
      body = body.substring(match.end());
    }
    
    match = GPS_PTN.matcher(body);
    if (match.lookingAt()) {
      String x = match.group(1);
      String y = match.group(2);
      if (!x.equals("0") || !y.equals("0")) {
        data.strGPSLoc = INT2WGS84(Double.parseDouble(x), Double.parseDouble(y));
      }
      body = body.substring(match.end());
    }
    
    match = EXTRA_PTN2.matcher(body);
    if (match.find()) {
      data.strSupp = append(data.strSupp, "\n", match.group(1).trim());
      body = body.substring(match.end()).trim();
    }
    
    match = ID_PTN.matcher(body);
    if (match.find()) {
      data.strCallId = match.group(1).replaceAll(" ", "");
      body = body.substring(0,match.start()).trim();
    } else {
      body = stripFieldEnd(body, "#");
    }
    
    body = stripFieldStart(body, ".");
    data.strCall = append(data.strCall, " / ", body);
    
    // Sometimes an intersection is reported as a cross street. 
    if (data.strAddress.length() == 0) {
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    
    // See if we can eliminate doubled addresses
    match = DOUBLED_ADDRESS.matcher(data.strAddress);
    if (match.matches()) data.strAddress = match.group(2);
    
    // Numeric place name is probably an apt.
    if (data.strApt.length() == 0 && PLACE_APT_PTN.matcher(data.strPlace).matches()) { 
      data.strApt = data.strPlace;
      data.strPlace = "";
    }
    
    if (data.strCallId.length() < 8) data.expectMore = true;
    
    return true;
  }
  

  /**
   * Convert Intergraph x/y coordinates to WGS84 lat/long string
   *
   * Intergraph uses a XY coordinate that is easily converted into NZTM.
   * The real work here is converting NZTM into a lat/lon value.
   * Math from http://www.linz.govt.nz/geodetic/conversion-coordinates/projection-conversions/transverse-mercator-preliminary-computations
   * @param xcoord the Intergraph X value (like 372512333)
   * @param ycoord the Intergraph Y value (like 238469543)
   * @return String containing a GPS pair like "-34.00,179.23"
   */
  static String INT2WGS84(double xcoord, double ycoord) {
  
    // Convert Intergraph format into NZTM (New Zealand Transverse Mercator)
    double e=(2147483647d-xcoord)/1000;
    double n=(6147483647d-ycoord)/1000;
    
    // For testing purposes, E=1808171 and N=5588209 should result in lat/lon ~ (-39.83063056,175.43261389)
    // FWIW, these are not the actual results, which doesn't sound very promising
    
    // double e=1808171; 
    // double n=5588209;
    
    double n_=n-N0;
    double e_=e-E0;
    double m_=M0+n_/K0;
    double sigma=(m_*Math.PI)/(180*G);
    double phi_=sigma+((3*NN/2)-(27*Math.pow(NN,3)/32))*Math.sin(2*sigma)+((21*Math.pow(NN,2)/16)-(55*Math.pow(NN,4)/32))*Math.sin(4*sigma)+(151*Math.pow(NN,3)/96)*Math.sin(6*sigma)+(1097*Math.pow(NN,4)/512)*Math.sin(8*sigma);
    double rho_=(A*(1-E_SQUARED))/Math.pow((1-(E_SQUARED*Math.pow(Math.sin(phi_),2))),(3/2));
    double nu_=A/Math.sqrt(1-(E_SQUARED*Math.pow(Math.sin(phi_),2)));
    double psi_=nu_/rho_;
    double t_=Math.tan(phi_);
    double x=e_/(K0*nu_);
    
    // Calculate latitude
    double t1=(t_*e_*x)/(K0*rho_*2);
    double t2=((t_*e_*Math.pow(x,3))/(K0*rho_*24))*((-4*Math.pow(psi_,2))+(9*psi_*(1-Math.pow(t_,2)))+(12*Math.pow(t_,2)));
    double t3=((t_*e_*Math.pow(x,5))/(K0*rho_*720))*((8*Math.pow(psi_,4)*(11-(24*Math.pow(t_,2))))-(12*Math.pow(psi_,3)*(21-71*Math.pow(t_,2)))+(15*Math.pow(psi_,2)*(15-98*Math.pow(t_,2)+15*Math.pow(t_,4)))+(180*psi_*(5*Math.pow(t_,2)-3*Math.pow(t_,4)))+(360*Math.pow(t_,4)));
    double t4=((t_*e_*Math.pow(x,7))/(K0*rho_*40320))*(1385-3633*Math.pow(t_,2)+4095*Math.pow(t_,4)+1575*Math.pow(t_,6));
    double lat=(180/Math.PI)*(phi_-t1+t2-t3+t4);
    
    // Calculate longitude
    t1=x*(1d/Math.cos(phi_));
    t2=((Math.pow(x,3)*(1d/Math.cos(phi_)))/6)*(phi_+2*Math.pow(t_,2));
    t3=((Math.pow(x,5)*(1d/Math.cos(phi_)))/120)*((-4*Math.pow(psi_,3)*(1-6*Math.pow(t_,2)))+(Math.pow(psi_,2)*(9-68*Math.pow(t_,2)))+(72*psi_*Math.pow(t_,2))+(24*Math.pow(t_,4)));
    t4=((Math.pow(x,7)*(1d/Math.cos(phi_)))/5040)*(61+662*Math.pow(t_,2)+1320*Math.pow(t_,4)+720*Math.pow(t_,6));
    double lon=(180/Math.PI)*(l0+t1-t2+t3-t4);
    
    // And convert the result to a GPS string
    return String.format("%+8.6f,%+8.6f", lat, lon);
  }
  
  // Build projection constants
  private static final double K0=0.9996;       // Scale
  private static final double A=6378137;       // Semimajor axis
  private static final double F=(1/298.257222101d);  // Flattening
  private static final double N0=10000000;       // False Northing
  private static final double E0=1600000;        // False Easting
  private static final double l0=173*(Math.PI/180);  // Origin longitude 173 degrees... I found this emperically
  private static final double B=A*(1-F);
  private static final double E_SQUARED=(2*F)-Math.pow(F,2);
  private static final double NN=(A-B)/(A+B);
  private static final double G=A*(1-NN)*(1-Math.pow(NN,2))*(1+(9*Math.pow(NN,2)/4)+(225*Math.pow(NN,4)/64))*(Math.PI/180);
  
  /* The following is not necessary since phi0 (origin latitude) is zero.  We just set m0=0 instead.  
  double A0=1-(e_squared/4)-(3*Math.pow(e_squared,2)/64)-(5*Math.pow(e_squared,4)/256);
  double A2=(3/8)*(e_squared+(Math.pow(e_squared,2)/4)+(15*Math.pow(e_squared,4)/128));
  double A4=(15/256)*(Math.pow(e_squared,2)+(3*Math.pow(e_squared,4)/4));
  double A6=35*Math.pow(e_squared,4)/3072;
  */
  private static final double M0=0;
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, CITY_MAP_TABLE);
  }

  static final Properties CITY_MAP_TABLE = buildCodeTable(new String[]{
      "LAKE ROTOMA",   "ROTOMA"
  });
  
  static final String[] MWORD_STREET_LIST = new String[]{
    "ABEL SMITH",
    "ABEL TASMAN",
    "ACACIA BAY",
    "AHI KAA",
    "ALAN MURRAY",
    "ALLEN MILLS",
    "ANISEED VALLEY",
    "ANZAC VALLEY",
    "AOTEA",
    "ARMS PARK",
    "AVONDAL E",
    "AWAHURI FEILDING",
    "BADDELEYS BEACH",
    "BALLANCE GORGE",
    "BALLANCE VALLEY",
    "BAY VIEW",
    "BEACH WATER",
    "BEATRICE TINSLEY",
    "BELLA VISTA",
    "BING LUCAS",
    "BLACK PINE",
    "BOOM ROCK",
    "BRICK BAY",
    "BRUCE MACGREGOR",
    "BRUCE MCLAREN",
    "BRUCE WALLACE",
    "BRYLEE DRIVE RESERVE BRYLEE",
    "CABBAGE TREE",
    "CABLE BAY",
    "CENTRAL PARK",
    "CHELTENHAM HUNTERVILLE",
    "CHURCH BAY",
    "COWAN BAY",
    "CROWN LYNN",
    "CROWNTHORPE SETTLEMENT",
    "DAN TORI",
    "DAVID WILLIAM",
    "DE MERLE",
    "DEEP CREEK",
    "DESERT GOLD",
    "DON MCKINNON",
    "DRIFT BAY",
    "EAST COAST",
    "EAST TAMAKI",
    "ELLERSLIE RACECOURSE",
    "EMERALD GLADE",
    "FAIR VIEW",
    "FAIRY SPRINGS",
    "FIVE MILE",
    "FLAG RANGE",
    "FORREST HILL",
    "FORT RICHARD",
    "FRANCES BROWN",
    "GEORGE BOLT MEMORIAL",
    "GLEN BROOK",
    "GLEN NEVIS STATION",
    "GORDON CRAIG",
    "GOVERNOR FITZROY",
    "GOVERNOR GREY",
    "GREAT BARRIER",
    "GREAT NORTH",
    "GREAT SOUTH",
    "GULF HARBOUR",
    "HALL BLOCK",
    "HAMPTON HILL",
    "HAWEA MOTOR CAMP",
    "HAYS CREEK",
    "HECTOR LANG",
    "HENRY CHARLES",
    "HUDSON BAY",
    "HUGO JOHNSTON",
    "HUKA FALLS",
    "HUNTERS PARK",
    "ISLAND VIEW",
    "IVER TRASK",
    "JACKS BUSH",
    "JAMES COOK",
    "JOAN WALLACE",
    "JOHNSON POINT",
    "JOSEPH BANKS",
    "KAHIKATEA FLAT",
    "KAIPARA COAST",
    "KAIPARA FLATS",
    "KAIPARA HILLS",
    "KAIPARA VIEW",
    "KAWAHA POINT",
    "KAWAU VIEW",
    "KHYBER PASS",
    "KOMOKORIKI HILL",
    "L PHILLIPS",
    "LAKE HAWEA-ALBERT TOWN",
    "LINCOLN PARK",
    "LOGAN CAMERON",
    "LONELY TRACK",
    "LONG MILE",
    "LONGFORD PARK",
    "MAHURANGI EAST",
    "MAHURANGI WEST",
    "MAIDA VALE",
    "MAIN NORTH",
    "MAIN SOUTH",
    "MAN OWAR BAY",
    "MANGAWHAI HEADS",
    "MARGARET REEVE",
    "MARINE VIEW",
    "MARKET COVE",
    "MARTINS BAY",
    "MARY ANN",
    "MATAKOHE EAST",
    "MATAKOHE WHARF",
    "MATE URLICH",
    "MAUNGAREI MEMORIAL",
    "MAXWELL STATION",
    "MCWHIRTERS FARM",
    "MOE HAU",
    "MOTUEKA VALLEY",
    "MOUNT EDEN",
    "MOUNT SMART",
    "MT EDGCUMBE",
    "MYSTERY CREEK",
    "NGA MANU RESERVE",
    "NGARUNUI BEACH",
    "NICK JOHNSTONE",
    "NORTH EYRE",
    "NORTH PARK",
    "NOVA SCOTIA",
    "OAK RIVER",
    "OCEAN VIEW",
    "OFF ROAD",
    "OKAREKA LOOP",
    "OKAWA BAY",
    "OKERE FALLS",
    "ONEHUNGA HARBOUR",
    "ONEROA VILLAGE",
    "OTEHA VALLEY",
    "OTOTOKA BEACH",
    "PACIFIC COAST",
    "PAEKAKARIKI HILL",
    "PAORA HAPI",
    "PAPAMOA BEACH",
    "PARADISE VALLEY",
    "PARK POINT",
    "PEKA PEKA",
    "PIEMELON BAY",
    "PIGEON MOUNTAIN",
    "PT CHEV",
    "PT ENGLAND",
    "PUHI HUIA",
    "PYES PA",
    "RANGITAIKI SCHOOL",
    "RANGITATAU EAST",
    "RANGIURU BAY",
    "RAUPO WHARF",
    "RED HIBISCUS",
    "RED HILLS",
    "RICHARD PEARSE",
    "RIFLE RANGE",
    "RIRIA KEREOPA MEMORIAL",
    "ROBERT MCKEEN",
    "RON KEAT",
    "RUAWAI SCHOOL",
    "RUAWAI WHARF",
    "SAN IGNACIO",
    "SAN PABLO",
    "SAN SEBASTIAN",
    "SAN VALENTINO",
    "SANDY BEACH",
    "SCHOLLUM ACCESS",
    "SEA VIEW",
    "SEL PEACOCK",
    "SHELLY BEACH",
    "SHEPPARD OAKS",
    "SHOAL BAY",
    "SMITH CANAL",
    "SNELLS BEACH",
    "SOUTH HEAD",
    "SOUTH TITIRANGI",
    "SPENCER RUSSELL",
    "ST HILL",
    "ST LUKES",
    "ST MARYS",
    "TAKAPUNA",
    "TAPAPA WEST",
    "TAURANGA DIRECT",
    "TE AHU AHU",
    "TE AKAU",
    "TE ARA",
    "TE ARAWI",
    "TE ATATU",
    "TE AWE AWE",
    "TE HAPUA",
    "TE HENGA",
    "TE HEUHEU",
    "TE HIKO",
    "TE HUTEWAI",
    "TE KAPA",
    "TE KOWHAI",
    "TE MAKIRI",
    "TE MANGA",
    "TE MATAI",
    "TE MIRO",
    "TE MOANA",
    "TE NGAE",
    "TE ONE",
    "TE PAI",
    "TE POI SOUTH",
    "TE PUIA",
    "TE RIMU",
    "TE TOKI",
    "TE TUHI",
    "TE WAIROA",
    "TE WHAU",
    "TE WIRIHANA",
    "TI RAKAU",
    "TIRI VIEW",
    "TITAHI BAY",
    "TRAVIS VIEW",
    "TREBLE CONE SKI FIELD ACCESS",
    "TRIG HILL",
    "TROUNSON PARK",
    "TWO CHAIN",
    "UTAUT - WAIKANAEFIRE A",
    "VINEGAR HILL",
    "VIV DAVIE-MARTIN",
    "WAIKANAE PARK PARK",
    "WAIKITE VALLEY",
    "WAIOTAPU LOOP",
    "WAIPA STATE MILL",
    "WAIWHIU CONICAL PEAK",
    "WALTER FRANK",
    "WALTER MACDONALD",
    "WANAKA-MOUNT ASPIRING",
    "WATT LIVINGSTONE",
    "WEST COAST",
    "WEST TAMAKI",
    "WESTERN BAY",
    "WHIRINAKI VALLEY",
    "WHITFORD BROWN",
    "WI NEERA",
    "WILLIAM PICKERING",
    "WILLIAM SOUTER",
    "WIRI STATION"
  };

  static final String[] CITY_LIST = new String[]{
    
    // Towns of new Zealand
    "AHAURA",
    "AHIPARA",
    "AHITITI",
    "AHURIRI",
    "AHUROA",
    "AIRPORT OAKS",
    "AKAROA",
    "AKITIO",
    "ALBANY",
    "ALBANY",
    "ALBERT TOWN",
    "ALBURY",
    "ALEXANDRA",
    "ALFRISTON",
    "ALGIES BAY",
    "ALLANTON",
    "AMBERLEY",
    "ANAKIWA",
    "ANNESBROOK",
    "AORANGI",
    "AOTEA",
    "ARAMOANA",
    "ARAMOHO",
    "ARANGA",
    "ARAPOHUE",
    "ARAPUNI",
    "ARARUA",
    "ARCH HILL",
    "ARDMORE",
    "ARKLES BAY",
    "ARMY BAY",
    "ARO VALLEY",
    "ARROWTOWN",
    "ARUNDEL",
    "ASCOT PARK",
    "ASHBURTON",
    "ASHHURST",
    "ASHLEY",
    "ATIAMURI",
    "AUCKLAND AIRPORT",
    "AUCKLAND CENTRAL",
    "AUCKLAND",
    "AUROA",
    "AVONDALE",
    "AWANUI",
    "AWAPUNI",
    "AWATOTO",
    "BALCLUTHA",
    "BALFOUR",
    "BALLANCE",
    "BALMORAL",
    "BARRYTOWN",
    "BAYSWATER",
    "BEACH HAVEN",
    "BEACHLANDS",
    "BEACHLANDS",
    "BEACONSFIELD",
    "BEAUMONT",
    "BELL BLOCK",
    "BELMONT",
    "BENHAR",
    "BENNEYDALE",
    "BIRKDALE",
    "BIRKENHEAD",
    "BLACKBALL",
    "BLENHEIM",
    "BLOCKHOUSE BAY",
    "BLUFF",
    "BOMBAY",
    "BOTANY DOWNS",
    "BOTANY DOWNS",
    "BRIGHTON",
    "BRIGHTWATER",
    "BROADLANDS FOREST",
    "BROADWOOD",
    "BROOKBY",
    "BROWNS BAY",
    "BRUNSWICK",
    "BUCKLANDS BEACH",
    "BULLS",
    "BUNNYTHORPE",
    "CAMBERLEY",
    "CAMBRIDGE",
    "CAMPBELLS BAY",
    "CANNONS CREEK",
    "CANVASTOWN",
    "CARTERTON",
    "CASTLECLIFF",
    "CASTOR BAY",
    "CHAPEL DOWNS",
    "CHARING CROSS",
    "CHARTWELL",
    "CHATSWOOD",
    "CHEVIOT",
    "CHRISTCHURCH",
    "CLARKSVILLE",
    "CLENDON",
    "CLEVEDON",
    "CLINTON",
    "CLIVE",
    "CLOVER PARK",
    "CLYDE",
    "COATESVILLE",
    "COBDEN",
    "COCKLE BAY",
    "COLLEGE ESTATE",
    "COLLINGWOOD",
    "COLVILLE",
    "CONIFER GROVE",
    "COROGLEN",
    "COROMANDEL",
    "CROFTON DOWNS",
    "CROMWELL",
    "CROWN HILL",
    "CROWNTHORPE",
    "CULVERDEN",
    "CUST",
    "CUTHILL",
    "DAIRY FLAT",
    "DANNEMORA",
    "DANNEVIRKE",
    "DARFIELD",
    "DARGAVILLE",
    "DEVONPORT",
    "DOBSON",
    "DRURY",
    "DUNEDIN CITY",
    "DUNEDIN",
    "DUNTROON",
    "DURIE HILL",
    "EAST PARKVALE",
    "EAST TAMAKI HEIGHTS",
    "EAST TAMAKI",
    "EASTBOURNE",
    "EASTERN BEACH",
    "EDEN TERRACE",
    "EDEN VALLEY",
    "EDENDALE",
    "EDGECUMBE",
    "EGMONT VILLAGE",
    "EKETAHUNA",
    "ELLERSLIE",
    "ELSDON",
    "ELTHAM",
    "ENNER GLYNN",
    "EPSOM",
    "ETTRICK",
    "FAIRFIELD",
    "FAIRHALL",
    "FAIRLIE",
    "FAIRVIEW DOWNS",
    "FAIRVIEW HEIGHTS",
    "FAIRVIEW",
    "FAIRY SPRINGS",
    "FARM COVE",
    "FAVONA",
    "FEATHERSTON",
    "FEILDING",
    "FENTON PARK",
    "FERNHILL",
    "FIVONA",
    "FLAMBORO HEIGHTS",
    "FLAT BUSH",
    "FLAXMERE",
    "FORDLANDS",
    "FORREST HILL",
    "FOX GLACIER",
    "FOXTON BEACH",
    "FOXTON",
    "FRANKTON",
    "FRANKTON",
    "FRANZ JOSEF",
    "FREEMANS BAY",
    "FRIMLEY",
    "GERALDINE",
    "GISBORNE",
    "GLEN EDEN",
    "GLEN INNES",
    "GLENDENE",
    "GLENDOWIE",
    "GLENFIELD",
    "GLENHOLME",
    "GLENORCHY",
    "GLENSIDE",
    "GLENVAR",
    "GOLFLANDS",
    "GONVILLE",
    "GOODWOOD HEIGHTS",
    "GORDONTON",
    "GORE",
    "GRAFTON",
    "GRANITY",
    "GREEN BAY",
    "GREENHITHE",
    "GREENLANE",
    "GREENMEADOWS",
    "GREENWOODS CORNER",
    "GREY LYNN",
    "GREYMOUTH",
    "GREYTOWN",
    "GROVETOWN",
    "GULF HARBOUR",
    "HAAST",
    "HAKATARAMEA",
    "HALCOMBE",
    "HALF MOON BAY",
    "HALSWELL",
    "HAMILTON",
    "HAMILTON CENTRAL",
    "HAMILTON CITY",
    "HAMILTON LAKE",
    "HAMPDEN",
    "HAMURANA",
    "HANMER SPRINGS",
    "HARI HARI",
    "HASTINGS",
    "HAUMOANA",
    "HAUPIRI",
    "HAVELOCK NORTH",
    "HAVELOCK",
    "HAWEA",
    "HAWERA",
    "HELENSVILLE",
    "HENDERSON",
    "HENLEY",
    "HERBERT",
    "HEREKINO",
    "HERNE BAY",
    "HERON POINT",
    "HIGHBURY",
    "HIGHLAND PARK",
    "HIKUAI",
    "HIKURANGI",
    "HIKUTAIA",
    "HILL PARK",
    "HILLCREST",
    "HILLSBOROUGH",
    "HILLTOP",
    "HINUERA",
    "HOBSONVILLE",
    "HOKITIKA",
    "HOKOWHITU",
    "HOPE",
    "HOREKE",
    "HOROHORO",
    "HOROKIWI",
    "HOUHORA",
    "HOWICK",
    "HOWICK",
    "HUAPAI",
    "HUIAKAMA",
    "HUIRANGI",
    "HUKERENUI",
    "HUNTERVILLE",
    "HUNTLY",
    "HUNUA",
    "HURLEYVILLE",
    "INANGAHUA JUNCTION",
    "INGLEWOOD",
    "INVERCARGILL",
    "IWITAHI",
    "JACOBS RIVER",
    "JOHNSONVILLE",
    "KAIAPOI",
    "KAIHU",
    "KAIKOHE",
    "KAIKOURA",
    "KAIMATA",
    "KAINGAROA",
    "KAIPARA FLATS",
    "KAITAIA",
    "KAITANGATA",
    "KAITOKE",
    "KAIWAKA",
    "KAKANUI",
    "KAKARAMEA",
    "KAMO",
    "KANIERE",
    "KAPONGA",
    "KARAMEA",
    "KARAPIRO",
    "KARETU",
    "KARITANE",
    "KARORI",
    "KATIKATI",
    "KAUKAPAKAPA",
    "KAURI",
    "KAWAHA POINT",
    "KAWAKAWA",
    "KAWERAU",
    "KAWHIA",
    "KELBURN",
    "KELSTON",
    "KENEPURU",
    "KENNEDY BAY",
    "KENSINGTON",
    "KERIKERI",
    "KIHIKIHI",
    "KAINGAROA FOREST",
    "KILBIRNIE",
    "KINGSLAND",
    "KINGSTON",
    "KINLEITH",
    "KINLOCH",
    "KOHIMARAMA",
    "KOKATAHI",
    "KOKOPU",
    "KONINI",
    "KOROMIKO",
    "KOUTU",
    "KUMARA",
    "KUMEU",
    "KUROW",
    "LAINGHOLM",
    "LAKE OKAREKA",
    "LAKE ROTOMA",
    "LANGS BEACH",
    "LAWRENCE",
    "LEESTON",
    "LEIGH",
    "LEPPERTON",
    "LEVIN",
    "LICHFIELD",
    "LINCOLN",
    "LINKWATER",
    "LINTON CAMP",
    "LITTLE RIVER",
    "LONG BAY",
    "LONGFORD PARK",
    "LOWER HUTT CITY",
    "LOWER HUTT",
    "LUGGATE",
    "LUMSDEN",
    "LYNFIELD",
    "LYNMORE",
    "LYTTELTON",
    "MAHIA PARK",
    "MAHORA",
    "MAHURANGI EAST",
    "MAIRANGI BAY",
    "MAKAHU",
    "MAMAKU",
    "MAKARAU",
    "MANAIA",
    "MANAIA",
    "MANAKAU",
    "MANAPOURI",
    "MANGAKAKAHI",
    "MANGAKINO",
    "MANGAMAHU",
    "MANGAMUKA",
    "MANGAPAI",
    "MANGATOKI",
    "MANGAWHAI HEADS",
    "MANGAWHAI",
    "MANGERE BRIDGE",
    "MANGERE EAST",
    "MANGERE",
    "MANLY",
    "MANUKAU HEIGHTS",
    "MANUKAU",
    "MANUKAU",
    "MANUREWA EAST",
    "MANUREWA",
    "MANUREWA",
    "MANUTAHI",
    "MAPUA",
    "MARAENUI",
    "MARAETAI",
    "MARAETAI",
    "MARCO",
    "MARLBOROUGH",
    "MAROMAKU",
    "MAROTIRI",
    "MARSDEN BAY",
    "MARTINBOROUGH",
    "MARTON",
    "MARUIA",
    "MARYBANK",
    "MASSEY",
    "MASTERTON",
    "MATAKANA",
    "MATAKATIA",
    "MATAKOHE",
    "MATAMATA",
    "MATAPU",
    "MATARANGI",
    "MATARAU",
    "MATATA",
    "MATAURA",
    "MATIHETIHE",
    "MAUNGAKARAMEA",
    "MAUNGATAPERE",
    "MAUNGATAUTARI",
    "MAUNGATUROTO",
    "MARAENUI",
    "MAXWELL",
    "MAYFAIR",
    "MAYFIELD",
    "MAYMORN",
    "MEADOWBANK",
    "MEADOWLANDS",
    "MEADOWOOD",
    "MELLONS BAY",
    "MEREMERE",
    "METHVEN",
    "MIDDLEMARCH",
    "MIDDLEMORE HOSPITAL",
    "MIDDLEMORE",
    "MIDHIRST",
    "MILFORD",
    "MILLERS FLAT",
    "MILSON",
    "MILTON",
    "MIMI",
    "MISSION BAY",
    "MOANA",
    "MOENUI",
    "MOERAKI",
    "MOEREWA",
    "MOKAI",
    "MOKAU",
    "MOKOIA",
    "MORNINGSIDE",
    "MORRINSVILLE",
    "MOSGIEL",
    "MOSSBURN",
    "MOTATAU",
    "MOTUEKA",
    "MOTUTERE",
    "MOUNT ALBERT",
    "MOUNT ASPIRING",
    "MOUNT COOK",
    "MOUNT EDEN",
    "MOUNT MAUNGANUI",
    "MOUNT ROSKILL",
    "MOUNT SOMERS",
    "MOUNT WELLINGTON",
    "MOUREA",
    "MURCHISON",
    "MURPHYS HEIGHTS",
    "MURRAYS BAY",
    "MURUPARA",
    "NAPIER CITY",
    "NAPIER SOUTH",
    "NAPIER",
    "NASEBY",
    "NELSON CITY",
    "NELSON",
    "NEW BRIGHTON",
    "NEW LYNN",
    "NEW PLYMOUTH",
    "NEW WINDSOR",
    "NEWMARKET",
    "NEWTOWN",
    "NGAERE",
    "NGAIO",
    "NGAKURU",
    "NGAMATAPOURI",
    "NGAPARA",
    "NGAPUNA",
    "NGARUAWAHIA",
    "NGATAKI",
    "NGATIRA",
    "NGAURANGA",
    "NGAWARO",
    "NGONGOTAHA VALLEY",
    "NGONGOTAHA",
    "NGUNGURU",
    "NIGHTCAPS",
    "NORFOLK",
    "NORMANBY",
    "NORTH HARBOUR",
    "NORTH PARK",
    "NORTHCOTE CENTRAL",
    "NORTHCOTE POINT",
    "NORTHCOTE",
    "NORTHCROSS",
    "NUKUHAU",
    "OAKLEIGH",
    "OAKURA",
    "OMAHA",
    "OAMARU",
    "OBAN",
    "OHAEAWAI",
    "OHAKUNE",
    "OHAKURI",
    "OHANGAI",
    "OHAUPO",
    "OHOKA",
    "OHOKA",
    "OHOPE BEACH",
    "OHURA",
    "OKAIHAU",
    "OKATO",
    "OKERE FALLS",
    "OKOIA",
    "OKOROIRE",
    "OMANAIA",
    "OMARAMA",
    "OMATA",
    "OMIHA",
    "OMOKOROA",
    "ONE TREE HILL",
    "ONEHUNGA",
    "ONEKAWA",
    "ONERAHI",
    "ONEROA",
    "ONETANGI",
    "ONEWHERO",
    "OPAHEKE",
    "OPONONI",
    "OPOTIKI",
    "OPUA",
    "OPUNAKE",
    "ORAKEI",
    "ORANGA",
    "ORATIA",
    "ORATIA",
    "ORERE POINT",
    "OREWA",
    "OROMAHOE",
    "OROPI",
    "ORUAITI",
    "OSTEND",
    "OTAHUHU",
    "OTAIKA",
    "OTAKI",
    "OTAKOU",
    "OTARA",
    "OTAUTAU",
    "OTEHA",
    "OTIRIA",
    "OTOROHANGA",
    "OWAIRAKA",
    "OWAKA",
    "OWHATA",
    "OXFORD",
    "PAEKAKARIKI HILL",
    "PAEKAKARIKI",
    "PAENGAROA",
    "PAERATA",
    "PAEROA",
    "PAHIATUA",
    "PAHUREHURE",
    "PAIHIA",
    "PAKARAKA",
    "PAKIRI",
    "PAKOTAI",
    "PAKOWHAI",
    "PAKURANGA HEIGHTS",
    "PAKURANGA",
    "PALM BEACH",
    "PALMERSTON NORTH",
    "PALMERSTON NORTH CITY",
    "PALMERSTON",
    "PAMAPURIA",
    "PANGURU",
    "PANDORA",
    "PANMURE",
    "PAPAKURA",
    "PAPAKURA",
    "PAPAMOA",
    "PAPAMOA BEACH",
    "PAPARANGI",
    "PAPAROA",
    "PAPARORE",
    "PAPATOETOE",
    "PAPATOETOE",
    "PARAKAI",
    "PARAPARAUMU BEACH",
    "PARAPARAUMU",
    "PAREMATA",
    "PAREMOREMO",
    "PAREORA",
    "PARIKINO",
    "PARNELL",
    "PAROA",
    "PARUA BAY",
    "PATEA",
    "PAUANUI",
    "PAUATAHANUI",
    "PEKA PEKA",
    "PEMBROKE",
    "PENROSE",
    "PERIA",
    "PETONE",
    "PIARERE",
    "PICTON",
    "PIHA",
    "PIHAMA",
    "PINEHILL",
    "PIOPIO",
    "PIPITEA",
    "PIPIWAI",
    "PIRIMAI",
    "PIRONGIA",
    "PLEASANT POINT",
    "PLIMMERTON",
    "POHUEHUE",
    "POINT CHEVALIER",
    "POINT ENGLAND",
    "POKENO",
    "POMARE",
    "PONSONBY",
    "PORAITI",
    "PORCHESTER PARK",
    "PORIRUA CITY CENTRE",
    "PORIRUA CITY",
    "PORIRUA",
    "POROTI",
    "PORT CHALMERS",
    "PORT TARANAKI",
    "PORTLAND",
    "PORTOBELLO",
    "PUHINUI",
    "PUHOI",
    "PUKEHANGI",
    "PUKEKOHE",
    "PUKEPOTO",
    "PUKERUA BAY",
    "PUKETAPU",
    "PUKEURI",
    "PURUA",
    "PUTARURU",
    "QUEENSTOWN",
    "RAETIHI",
    "RAGLAN",
    "RAHOTU",
    "RAI VALLEY",
    "RAINBOW POINT",
    "RAMARAMA",
    "RANDWICK PARK",
    "RANFURLY",
    "RANGATIRA PARK",
    "RANGIORA",
    "RANUI",
    "RAPAURA",
    "RATANA",
    "RATAPIKO",
    "RAUMATI",
    "RAUMATI BEACH",
    "RAUREKA",
    "RAWENE",
    "RAWHITIROA",
    "RED HILL",
    "REDOUBT PARK",
    "REDVALE",
    "REEFTON",
    "REMUERA",
    "RENWICK",
    "REPOROA",
    "RICHMOND HEIGHTS",
    "RICHMOND PARK",
    "RICHMOND",
    "RIVERHEAD",
    "RIVERLANDS",
    "RIVERSDALE BEACH",
    "RIVERTON",
    "RIWAKA",
    "ROLLESTON",
    "ROSEDALE",
    "ROSEHILL",
    "ROSLYN",
    "ROSS",
    "ROTHESAY BAY",
    "ROTOITI FOREST",
    "ROTOKAWA",
    "ROTOMA",
    "ROTORUA",
    "ROXBURGH",
    "ROYAL OAK",
    "RUAKAKA",
    "RUATORIA",
    "RUAWAI",
    "RUNANGA",
    "RUSSELL",
    "SAINT ANDREWS CANTERBURY",
    "SAINT ANDREWS",
    "SAINT ARNAUD",
    "SAINT BATHANS",
    "SAINT HELIERS",
    "SAINT JOHNS HILL",
    "SAINT JOHNS",
    "SAINT LEONARDS",
    "SAINT MARYS BAY",
    "SANDRINGHAM",
    "SANDSPIT",
    "SANSON",
    "SEACLIFF",
    "SEDDON",
    "SEDDONVILLE",
    "SETTLERS COVE",
    "SHAMROCK PARK",
    "SHANNON",
    "SHEFFIELD",
    "SHELLY PARK",
    "SHERENDEN",
    "SILKWOOD HEIGHTS",
    "SILVERDALE",
    "SNELLS BEACH",
    "SOMERVILLE",
    "SOMMERVILLE",
    "SOUTH HEAD",
    "SPOTSWOOD",
    "SPRING CREEK",
    "SPRINGFIELD",
    "SPRINGSTON",
    "SPRINGVALE",
    "ST ANDREWS CANTERBURY",
    "ST ARNAUD",
    "ST BATHANS",
    "ST HELIERS",
    "ST JOHNS HILL",
    "ST JOHNS",
    "ST MARYS BAY",
    "STANLEY BAY",
    "STANMORE BAY",
    "STIRLING",
    "STOKE",
    "STRATFORD",
    "SUNNYNOOK",
    "SUNNYVALE",
    "SURFDALE",
    "SWANNANOA",
    "SWANSON",
    "TAHAROA",
    "TAHEKE",
    "TAIERI MOUTH",
    "TAIHAPE",
    "TAIPA",
    "TAIPA-MANGONUI",
    "TAIRUA",
    "TAKAKA",
    "TAKANINI",
    "TAKAPUNA",
    "TAKAPUWAHIA",
    "TAMAKI",
    "TAMATEA",
    "TANGIMOANA",
    "TANGITERORIA",
    "TANGOWAHINE",
    "TAPANUI",
    "TAPAPA",
    "TAPAWERA",
    "TAPORA",
    "TAPU",
    "TARADALE",
    "TARAWERA",
    "TAUHARA",
    "TAUHOA",
    "TAUMARUNUI",
    "TAUPAKI",
    "TAUPIRI",
    "TAUPO",
    "TAURANGA CITY",
    "TAURANGA SOUTH",
    "TAURANGA",
    "TAURAROA",
    "TAUTORO",
    "TAWA",
    "TAWHARANUI PENINSULA",
    "TAWHERO",
    "TE ANAU",
    "TE ARAI",
    "TE AROHA",
    "TE ATATU PENINSULA",
    "TE ATATU SOUTH",
    "TE ATATU",
    "TE ATTU SOUTH",
    "TE AWAMUTU",
    "TE HAPUA",
    "TE HORO",
    "TE KAO",
    "TE KOPURU",
    "TE KOWHAI",
    "TE KUITI",
    "TE PAPAPA",
    "TE POI",
    "TE PUKE",
    "TE PURU",
    "TE RERENGA",
    "TEMUKA",
    "TERRACE END",
    "THAMES",
    "THE GARDENS",
    "THREE KINGS",
    "TIHOI",
    "TIKIPUNGA",
    "TIKITERE",
    "TIKORANGI",
    "TIMARU",
    "TINDALLS BEACH",
    "TINOPAI",
    "TINWALD",
    "TIRAU",
    "TITAHI BAY",
    "TITIRANGI",
    "TITOKI",
    "TOKAANU",
    "TOKANUI",
    "TOKARAHI",
    "TOKO",
    "TOKOMARU",
    "TOKOROA",
    "TOLAGA BAY",
    "TOMARATA",
    "TORBAY",
    "TOTARA HEIGHTS",
    "TOWAI",
    "TUAKAU",
    "TUAMARINA",
    "TUATAPERE",
    "TUKI TUKI",
    "TURANGI",
    "TUSCANY ESTATE",
    "TWO MILE BAY",
    "TWYFORD",
    "TWIZEL",
    "UMAWERA",
    "UNSWORTH HEIGHTS",
    "UPPER HUTT CITY",
    "UPPER HUTT",
    "UPPER MOUTERE",
    "URENUI",
    "URUTI",
    "UTUHINA",
    "VICTORIA",
    "WADDINGTON",
    "WAHAROA",
    "WAIAKE",
    "WAIATARUA",
    "WAIHARARA",
    "WAIHEKE ISLAND",
    "WAIHI BEACH",
    "WAIHI",
    "WAIHOLA",
    "WAIHOU",
    "WAIKANAE BEACH",
    "WAIKANAE",
    "WAIKAWA",
    "WAIKAWA",
    "WAIKOUAITI",
    "WAIKOUAITI",
    "WAIKOWHAI",
    "WAIKUKU",
    "WAIMA",
    "WAIMAHIA LANDING",
    "WAIMANGAROA",
    "WAIMARAMA",
    "WAIMATE NORTH",
    "WAIMATE",
    "WAIMAUKU",
    "WAINGARO",
    "WAINUI",
    "WAINUIOMATA",
    "WAIOHIKI",
    "WAIONEKE",
    "WAIORUA BAY",
    "WAIOTAPU",
    "WAIOTIRA",
    "WAIOURU",
    "WAIPAWA",
    "WAIPU",
    "WAIPUKURAU",
    "WAIPUNGA",
    "WAIRAKEI",
    "WAIRAU VALLEY",
    "WAIRAU VALLEY",
    "WAIROA",
    "WAITAHANUI",
    "WAITAHUNA",
    "WAITAKERE",
    "WAITANGIRUA",
    "WAITARA",
    "WAITARIA BAY",
    "WAITATI",
    "WAITOA",
    "WAITOKI",
    "WAITORIKI",
    "WAITOTARA",
    "WAIUKU",
    "WAIWERA",
    "WAKEFIELD",
    "WALLACETOWN",
    "WALTON",
    "WANAKA",
    "WANGANUI",
    "WHANGANUI AIRPORT",
    "WANGANUI EAST",
    "WARD",
    "WARDVILLE",
    "WARKWORTH",
    "WARRINGTON",
    "WATERVIEW",
    "WATTLE COVE",
    "WATTLE DOWNS",
    "WAVERLEY",
    "WELLINGTON CENTRAL",
    "WELLINGTON CITY",
    "WELLINGTON",
    "WELLSFORD",
    "WEST END",
    "WEST HARBOUR",
    "WESTERN HEIGHTS",
    "WESTERN SPRINGS",
    "WESTFIELD",
    "WESTGATE",
    "WESTLAKE",
    "WESTMERE",
    "WESTON",
    "WESTPORT",
    "WEYMOUTH",
    "WHAKAMARU",
    "WHAKATU",
    "WHAKAREWAREWA",
    "WHAKATANE",
    "WHANANAKI",
    "WHANGAEHU",
    "WHANGAMATA",
    "WHANGAMOMONA",
    "WHANGANUI",
    "WHANGANUI EAST",
    "WHANGAREI HEADS",
    "WHANGAREI",
    "WHANGARURU",
    "WHAREWAKA",
    "WHATAROA",
    "WHATUWHIWHI",
    "WHENUAKITE",
    "WHENUAKURA",
    "WHENUAPAI",
    "WHIRITOA",
    "WHITBY",
    "WHITFORD",
    "WHITIANGA",
    "WILLOWBY",
    "WIMBLEDON",
    "WINCHESTER",
    "WINDSOR PARK",
    "WINDSOR",
    "WINDWHISTLE",
    "WINSCOMBE",
    "WINTON",
    "WINTON",
    "WIRI",
    "WOODEND",
    "WOODHILL",
    "WOODRIDGE",
    "WOODVILLE",
    "WYNDHAM",

    // Districts
    "ASHBURTON DISTRICT",
    "BULLER DISTRICT",
    "CARTERTON DISTRICT",
    "CENTRAL HAWKES BAY DISTRICT",
    "CENTRAL OTAGO DISTRICT",
    "CLUTHA DISTRICT",
    "FAR NORTH DISTRICT",
    "GISBORNE DISTRICT",
    "GORE DISTRICT",
    "GREY DISTRICT",
    "HASTINGS DISTRICT",
    "HAURAKI DISTRICT",
    "HOROWHENUA DISTRICT",
    "HURUNUI DISTRICT",
    "KAIKOURA DISTRICT",
    "KAIPARA DISTRICT",
    "KAPITI COAST DISTRICT",
    "KAWERAU DISTRICT",
    "MACKENZIE DISTRICT",
    "MANAWATU DISTRICT",
    "MARLBOROUGH DISTRICT",
    "MASTERTON DISTRICT",
    "MATAMATA-PIAKO DISTRICT",
    "NEW PLYMOUTH DISTRICT",
    "NORTH HASTINGS DISTRICT",
    "OPOTIKI DISTRICT",
    "OTOROHANGA DISTRICT",
    "QUEENSTOWN-LAKES DISTRICT",
    "RANGITIKEI DISTRICT",
    "ROTORUA DISTRICT",
    "RUAPEHU DISTRICT",
    "SELWYN DISTRICT",
    "SOUTH TARANAKI DISTRICT",
    "SOUTH WAIKATO DISTRICT",
    "SOUTH WAIRARAPA DISTRICT",
    "SOUTHLAND DISTRICT",
    "STRATFORD DISTRICT",
    "TARARUA DISTRICT",
    "TASMAN DISTRICT",
    "TAUPO DISTRICT",
    "THAMES-COROMANDEL DISTRICT",
    "TIMARU DISTRICT",
    "WAIKATO DISTRICT",
    "WAIMAKARIRI DISTRICT",
    "WAIMATE DISTRICT",
    "WAIPA DISTRICT",
    "WAIROA DISTRICT",
    "WAITAKI DISTRICT",
    "WAITOMO DISTRICT",
    "WHANGANUI DISTRICT",
    "WESTERN BAY OF PLENTY DISTRICT",
    "WESTLAND DISTRICT",
    "WHAKATANE DISTRICT",
    "WHANGAREI DISTRICT"
  };
}
