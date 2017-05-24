package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;


public class OHTrentonParser extends DispatchEmergitechParser {
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(.*?) +(?:-(?:\\d+|TRAIN))?\\((\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d(:\\d\\d)? [AP]M)\\)");
  private static final DateFormat TIME_FMT1 = new SimpleDateFormat("hh:mm:ss aa"); 
  private static final DateFormat TIME_FMT2 = new SimpleDateFormat("hh:mm aa"); 
  
  public OHTrentonParser() {
    super("", CITY_LIST, "TRENTON", "OH");
  }

  @Override
  public String getFilter() {
    return "paging@ci.trenton.oh.us,@cityoftrenton.com";
  }
  
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = DATE_TIME_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strDate = match.group(2);
      setTime(match.group(4) != null ? TIME_FMT1 :  TIME_FMT2, match.group(3), data);
    }
    return super.parseMsg(subject, body, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }

  private static final String[] CITY_LIST = new String[]{
      "ALERT",
      "AMANDA",
      "BECKETT RIDGE",
      "BETHANY",
      "BLUE BALL",
      "COLLEGE CORNER",
      "COLLINSVILLE",
      "DARRTOWN",
      "ENGLE'S CORNER",
      "EXCELLO",
      "FAIRFIELD",
      "FOUR BRIDGES",
      "HAMILTON",
      "HANOVER",
      "HERITAGE",
      "INDIAN SPRINGS",
      "JACKSONBURG",
      "LEMON",
      "LESOURDESVILLE",
      "LIBERTY",
      "MADISON",
      "MAUD",
      "MCGONIGLE",
      "MIDDLETOWN",
      "MILFORD",
      "MILLVILLE",
      "MILTONVILLE",
      "MONROE",
      "MORGAN",
      "NEW MIAMI",
      "OKEANA",
      "OLDE WEST CHESTER",
      "ONEIDA",
      "OVERPECK",
      "OXFORD",
      "PISGAH",
      "POASTTOWN",
      "PORT UNION",
      "PRINCETON",
      "REILY",
      "ROSS",
      "SCIPIO",
      "SEVEN MILE",
      "SHANDON",
      "SHARONVILLE",
      "SOMERVILLE",
      "ST CLAIR",
      "TRENTON",
      "TYLERSVILLE",
      "WAYNE",
      "WEST CHESTER",
      "WEST MIDDLETOWN",
      "WETHERINGTON",
      "WILLIAMSDALE",
      "WOODSDALE"
  };
}
