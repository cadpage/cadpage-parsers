package net.anei.cadpage.parsers.IN;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA6Parser;

/**
 * St Joseph County, IN
 */
public class INStJosephCountyAParser extends DispatchA6Parser {

  public INStJosephCountyAParser() {
    super(CITY_CODES, "ST JOSEPH COUNTY", "IN");
    setupSpecialStreets("NEW RD");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
    };
  }

  @Override
  public String getFilter() {
    return "cad@sjc.com,@c-msg.net,@etieline.com,5742617686,5742922865,5742081200";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  // Pattern we use to try to find the missing space between the cross street and info
  private static final Pattern DATE_ADDR_BRK = Pattern.compile(" \\d\\d/\\d\\d/\\d\\d(?<!20)(?=[A-Z0-9])");
  private static final Pattern CROSS_BREAK = Pattern.compile("\\)[ A-Z0-9]+? (?:(?:RD|HW)(?! )|(?:ST|AV|TR|DR)(?![AEIOU ]))");
  private static final Pattern MAP_PTN = Pattern.compile(" +([A-Z]-\\d+|\\d{2,3}-\\d{2,3})");
  private static final Pattern LEAD_DATE_TIME = Pattern.compile("^(?:(\\d\\d?:\\d\\d(?::\\d\\d|[AP]M)) )?(\\d\\d/\\d\\d/\\d{4}) ");
  private static final Pattern TRAIL_TIME = Pattern.compile(" ([012]\\d)(\\d\\d),?$");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mmaa");;
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile("(?:[ ,]*\\b(?:[A-Z]+\\d+|\\d{4}|OSCEO|PENN|SWPCA))+$");
  private static final Pattern TRAIL_UNIT_BRK_PTN = Pattern.compile("[ ,]+");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // This is going to be interesting.  Different departments get radically different page formats,
    // but they all share the same sender space, and all have some features in common.  All of which
    // makes me thing they are originating from the  same dispatch center with some interesting post
    // generation changes.  We will try to handle them all in one text parser

    body = stripFieldStart(body, "- ");

    // Now they don't put a space between a date and the address that follows it :(
    Matcher match = DATE_ADDR_BRK.matcher(body);
    if (match.find()) {
      int pt = match.end();
      body = body.substring(0,pt) + " " + body.substring(pt);
    }

    // These guys don't put a space between the cross street an info field.
    // So to find where it should be and restore it
    match = CROSS_BREAK.matcher(body);
    if (match.find()) {
      int pt = match.end();
      body = body.substring(0,pt) + " " + body.substring(pt);
    }

    match = LEAD_DATE_TIME.matcher(body);
    if (match.lookingAt()) {
      String time = match.group(1);
      if (time != null) {
        if (time.endsWith("M")) {
          setTime(TIME_FMT, time, data);
        } else {
          data.strTime = time;
        }
      }
      data.strDate = match.group(2);
      body = body.substring(match.end()).trim();
    }

    else {
      match = TRAIL_TIME.matcher(body);
      if (match.find()) {
        data.strTime = match.group(1) + ':' + match.group(2);
        body = body.substring(0,match.start()).trim();
      }
    }
    if (! super.parseMsg(body, data)) return false;
    data.strAddress = data.strAddress.replace("*", "");
    match = MAP_PTN.matcher(data.strCall);
    if (match.find()) {
      data.strMap = match.group(1);
      data.strCall = data.strCall.substring(0,match.start());
    }

    if (data.msgType == MsgType.PAGE) {
      match = TRAIL_UNIT_PTN.matcher(data.strSupp);
      if (match.find()) {
        String unit = match.group().trim();
        data.strSupp = data.strSupp.substring(0,match.start()).trim();
        data.strSupp = stripFieldEnd(data.strSupp, " /");
        unit = stripFieldStart(unit, ",");
        unit = TRAIL_UNIT_BRK_PTN.matcher(unit).replaceAll(",");
        data.strUnit = append(data.strUnit, ",", unit);
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return  "TIME DATE " + super.getProgram();
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "CE", "CENTRE TWP",
      "CL", "CLAY TWP",
      "GE", "GERMAN TWP",
      "GR", "GREEN TWP",
      "HA", "HARRIS TWP",
      "LA", "LAKEVILLE",
      "LI", "LIBERTY TWP",
      "MA", "MADISON TWP",
      "MI", "MISHAWAKA",
      "NC", "NEW CARLISLE",
      "ND", "NOTRE DAME",
      "OS", "OSCEOLA",
      "PE", "PENN TWP",
      "PO", "PORTAGE TWP",
      "SB", "SOUTH BEND",
      "SO", "SW CENTRAL",
      "SW", "",
      "WA", "WARREN TWP",
      "WK", "WALKERTON"

  });
}
