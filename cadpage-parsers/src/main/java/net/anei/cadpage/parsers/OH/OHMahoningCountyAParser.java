package net.anei.cadpage.parsers.OH;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;


public class OHMahoningCountyAParser extends DispatchEmergitechParser {

  public OHMahoningCountyAParser() {
    this("MAHONING COUNTY", "OH");
  }

  public OHMahoningCountyAParser(String defCity, String defState) {
    super(true, CITY_LIST, defCity, defState, EMG_FLG_NO_PLACE, TrailAddrType.INFO);
  }

  @Override
  public String getAliasCode() {
    return "OHMahoningCountyA";
  }

  @Override
  public String getFilter() {
    return "BPD911@twp.boardman.oh.us,canfieldpd@ci.canfield.oh.us,jonracco25@yahoo.com,@sebringohio.net,@sebringohiopolice.com,@zoominternet.net,@canfield.gov";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern MARK_ID_PTN = Pattern.compile("(\\d+):");
  private static final Pattern MISSING_BRACKET_PTN = Pattern.compile("\\d+\\]");
  private static final Pattern CALL_OUT_PFX_PTN = Pattern.compile("[* ]*(?:(?:CALL OUTE?\\b[, ]*)+|MEDIC NEEDED|CORRECTED ADDRESS)[*: ]*");
  private static final Pattern BAD_UNIT_PFX_PTN = Pattern.compile("[A-Z0-9]+\\]");
  private static final Pattern N_GEORGETOWN_PTN = Pattern.compile(" \\(N\\.? GEORGETOWN\\) ");
  private static final Pattern TRUNC_GPS_PTN = Pattern.compile("[-+]?[\\d\\.]+ CF= *\\d+% UF= *\\d+ M Z= *[.0-9]* ?M\\b *");
  private static final Pattern BAD_PLACE_PTN = Pattern.compile(".*(?:EXIT|[/&])");
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *[-_]{3,} *");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (subject.startsWith("Automatic R&R Notification:")) return false;

    body = stripFieldStart(body, "contact:");
    Matcher match = MARK_ID_PTN.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end()).trim();
      if (subject.equals("NATURE") || subject.equals("LOCATION")) {
        subject = '[' + match.group(1) + "]- " + subject;
      }
    }

    else if (MISSING_BRACKET_PTN.matcher(body).lookingAt()) {
      body = '[' + body;
    }

    if (subject.endsWith("- CALL")) {
      body = subject + ':' + body;
      subject = "";
    }

    match = CALL_OUT_PFX_PTN.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end());
      if (!body.contains("LOCATION:")) body = "NATURE: CALL OUT LOCATION: " + body;
    }

    if (BAD_UNIT_PFX_PTN.matcher(body).lookingAt()) body = '[' + body;

    body = N_GEORGETOWN_PTN.matcher(body).replaceAll(" ");

    body = body.replace('\n', ' ');
    if (!super.parseMsg(subject, body,  data)) return false;

    String call = CALL_CODES.getProperty(data.strCall);
    if (call != null) {
      data.strCode = data.strCall;
      data.strCall = call;
    }

    if (BAD_PLACE_PTN.matcher(data.strPlace).matches()) {
      if (!data.strPlace.endsWith("EXIT")) data.strPlace = data.strPlace.substring(0,data.strPlace.length()-1).trim();
      data.strAddress = append(data.strPlace, " & ", data.strAddress);
      data.strPlace = "";
    }

    data.strCity = data.strCity.toUpperCase();
    data.strCity = convertCodes(data.strCity, CITY_FIXES);
    if (PA_CITIES.contains(data.strCity)) data.strState = "PA";

    match = TRUNC_GPS_PTN.matcher(data.strSupp);
    if (match.lookingAt()) data.strSupp = data.strSupp.substring(match.end());

    data.strSupp = INFO_BRK_PTN.matcher(data.strSupp).replaceAll("\n").trim();

    return true;
  }

  @Override
  public String getProgram() {
    return "CODE " + super.getProgram().replace("CITY", "CITY ST") + " GPS";
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "ALLIANCE",
    "CAMPBELL",
    "CANFIELD",
    "COLUMBIANA",
    "SALEM",
    "STRUTHERS",
    "YOUNGSTOWN",
    "YOUNGSTOW N",

    // Villages
    "BELOIT",
    "CRAIG BEACH",
    "LOWELLVILLE",
    "NEW MIDDLETOWN",
    "POLAND",
    "SEBRING",
    "WASHINGTONVILLE",

    // Townships
    "AUSTINTOWN TWP",
    "BEAVER TWP",
    "BERLIN TWP",
    "BOARDMAN TWP",
    "CANFIELD TWP",
    "COITSVILLE TWP",
    "COITSVILL E TWP",
    "ELLSWORTH TWP",
    "GOSHEN TWP",
    "GREEN TWP",
    "JACKSON TWP",
    "MILTON TWP",
    "POLAND TWP",
    "SMITH TWP",
    "SPRINGFIELD TWP",

    "BEAVER",
    "BERLIN",
    "CANFIELD",
    "COITSVILLE",
    "ELLSWORTH",
    "GOSHEN",
    "GREEN",
    "JACKSON",
    "MILTON",
    "POLAND",
    "SMITH",
    "SPRINGFIELD",

    // Census-designated places
    "AUSTINTOWN",
    "BOARDMAN",
    "MAPLE RIDGE",
    "MINERAL RIDGE",

    //Other communities
    "DAMASCUS",
    "ELLSWORTH",
    "GREENFORD",
    "LAKE MILTON",
    "NEW SPRINGFIELD",
    "NORTH BENTON",
    "N BENTON",
    "NORTH JACKSON",
    "N JACKSON",
    "NORTH LIMA",
    "PETERSBURG",

    // Columbiana County
    // Cities
    "COLUMBIANA",
    "EAST LIVERPOOL",
    "SALEM",

    // Villages
    "EAST PALESTINE",
    "HANOVERTON",
    "LEETONIA",
    "LISBON",
    "MINERVA",
    "NEW WATERFORD",
    "ROGERS",
    "SALINEVILLE",
    "SUMMITVILLE",
    "WASHINGTONVILLE",
    "WELLSVILLE",

    // Townships
    "BUTLER TWP",
    "CENTER TWP",
    "ELKRUN TWP",
    "FAIRFIELD TWP",
    "FRANKLIN TWP",
    "HANOVER TWP",
    "KNOX TWP",
    "LIVERPOOL TWP",
    "MADISON TWP",
    "MIDDLETON TWP",
    "PERRY TWP",
    "SALEM TWP",
    "ST. CLAIR TWP",
    "UNITY TWP",
    "WASHINGTON TWP",
    "WAYNE TWP",
    "WEST TWP",
    "YELLOW CREEK TWP",

    // Census-designated places
    "CALCUTTA",
    "DAMASCUS",
    "DAMUSCUS",  // misspelled
    "EAST ROCHESTER",
    "GLENMOOR",
    "HOMEWORTH",
    "LA CROFT",
    "LAKE TOMAHAWK",
    "NEGLEY",

    // Unincorporated communities
    "ACHOR",
    "BAYARD",
    "BERLIN CENTER",
    "CANNONS MILL",
    "CHAMBERSBURG",
    "CLARKSON",
    "DUNGANNON",
    "EAST CARMEL",
    "EAST FAIRFIELD",
    "ELKTON",
    "FRANKLIN SQUARE",
    "FREDERICKTOWN",
    "GAVERS",
    "GLASGOW",
    "GUILFORD",
    "HIGHLANDTOWN",
    "KENSINGTON",
    "LYNCHBURG",
    "MIDDLETON",
    "MILL ROCK",
    "MILLPORT",
    "MOULTRIE",
    "NEW ALEXANDER",
    "NEW GARDEN",
    "NEW MIDDLETON",
    "NEW SALISBURY",
    "NORTH GEORGETOWN",
    "READING",
    "TEEGARDEN",
    "UNIONVILLE",
    "UNITY",
    "VALLEY",
    "WEST POINT",
    "WILLIAMSPORT",
    "WINONA",

    // Columbia County
    "BUTLER TWP",
    "FAIRFIELD TWP",
    "KNOX TWP",
    "PERRY TWP",
    "SALEM TWP",
    "UNIT TWP",

    "BUTLER",
    "FAIRFIELD",
    "KNOX",
    "PERRY",
    "UNIT",

    "COLUMBIANA",
    "EAST PALESTINE",
    "LEETONIA",
    "NEW WATERFORD",
    "SALEM",
    "WASHINGTONVILLE",

    // Lawrence County, PA
    "LITTLE BEAVER TWP",
    "MAHONING TWP",
    "NORTH BEAVER TWP",
    "PULASKI TWP",
    "BESSEMER",
    "SNPJ",
    "ENON VALLEY",

    // Mercer County, PA
    "GREENIE TWP",
    "WEST SALEM TWP",
    "SOUTH PYMATUNING TWP",
    "SHENANGO TWP",
    "SHARPSVILLE",
    "SHARRON",
    "HERMITAGE",
    "FARRELL",
    "WHEATLAND",
    "WEST MIDDLESEX",

    // Portage County
    "DEERFIELD",
    "DEERFIELD TWP",
    "DIAMOND",
    "PALMYRA",
    "PALMYRA TWP",

    // Stark County
    "LEXINGTON TWP",
    "LEXINGTON",

    // Trumbull County
    "HUBBARD TWP",
    "LIBERTY TWP",
    "LORDSTOWN TWP",
    "NEWTON TWP",
    "WEATHERSFIELD TWP",
    "LIBERTY",
    "LORDSTOWN",
    "NEWTON",
    "WEATHERSFIELD",

    "CHURCHILL",
    "GIRARD",
    "HUBBARD",
    "HILLTOP",
    "HOWLAND",
    "MAPLEWOOD PARK",
    "NILES",
    "MCDONALD",
    "NEWTON FALLS",
    "SOUTH CANAL"
  };

  private static final Set<String> PA_CITIES = new HashSet<String>(Arrays.asList(

      // Lawrence County, PA
      "LITTLE BEAVER TWP",
      "MAHONING TWP",
      "NORTH BEAVER TWP",
      "PULASKI TWP",
      "BESSEMER",
      "SNPJ",
      "ENON VALLEY",

      // Mercer County, PA
      "GREENIE TWP",
      "WEST SALEM TWP",
      "SOUTH PYMATUNING TWP",
      "SHENANGO TWP",
      "SHARPSVILLE",
      "SHARRON",
      "HERMITAGE",
      "FARRELL",
      "WHEATLAND",
      "WEST MIDDLESEX"
  ));

  private static final Properties CITY_FIXES = buildCodeTable(new String[]{
      "DAMUSCUS",       "DAMASCUS"
  });



  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "6A",    "Accident with Injury",
      "F29",   "Medical Call without FD Response",
      "F30",   "Transfer to Lanes",
      "F31",   "Medical Call with FD Response",
      "F40",   "Arson Call Out",
      "F42",   "Burning Complaint",
      "F86",   "Odor Complaint",
      "F87",   "Misc. FD Call",
      "F88",   "Township Fuel Spills",
      "F89",   "Freeway Call",
      "F90",   "Fire Drill",
      "F91",   "Structure Fire",
      "F92",   "Vehicle Fire",
      "F93",   "Grass/Brush Fire",
      "F94",   "Jaws Call",
      "F95",   "Dumpster Fire Near Building",
      "F96",   "Dumpster Fire Not Near Building",
      "F97",   "Fire Alarm",
      "F97A",  "Trouble Alarm",
      "F98",   "Mutual Aid",
      "F99",   "Chemical Spill",
      "F101",  "Carbon Monoxide Check",
      "F102",  "Water Rescue"
  });
}
