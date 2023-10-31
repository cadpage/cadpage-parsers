package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOStLouisCountyCParser extends FieldProgramParser {

  public MOStLouisCountyCParser() {
    super("ST LOUIS COUNTY", "MO",
        "( SELECT/2 UNIT_RESP On:CALL! AT:ADDR! APT:APT? BUS:PLACE? CHL:CH! FD:SRC " +
        "| CALL! AT:ADDR! APT:APT? BUS:PLACE? XST:X BUS:PLACE )");
  }

  @Override
  public String getFilter() {
    return "dispatch@cce911.org,paging@cce911.org,93001";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern BAD_MSG_PTN = Pattern.compile(".* AT .* BUS:.*Units:");
  private static final Pattern AT_PTN = Pattern.compile(" AT[^A-Z]");
  private static final Pattern RESPONDING_PTN = Pattern.compile("(.*?) (?:Is|Are) Responding (.*?) (?=On:)");
  private static final Pattern CHANNEL_PTN = Pattern.compile("[, ]*(?:TAC: *)?((?:CC911 )?(?:SO|NO) - (?:S MAIN F|N MAIN F|FTAC \\d{2}))");
  private static final Pattern DUP_ALERT_PTN = Pattern.compile("(.*?(?: \\[ | Units:) *[^ ]*).*(?: \\[ | Units:) *[^ ]*(.*)");
  private static final Pattern ID_PTN = Pattern.compile(" *(?:Incident:)?(\\d{2}-\\d+)$");
  private static final Pattern TIME_PTN = Pattern.compile(" +(\\d\\d:\\d\\d)$");
  private static final Pattern MAP_PTN = Pattern.compile(" +([A-Z]+ ZONE\\d(?: - [ A-Z0-9]+)?)$");
  private static final Pattern LAT_LONG_PTN = Pattern.compile("(?:(38)(\\d{6}) +(\\d{2})(\\d{6})| 0 0)$");
  private static final Pattern UNIT_SRC_PTN = Pattern.compile("Unit\\(s\\):(\\S*(?: [A-Z]+\\d+)?)(?: +(\\d\\d))?$");
  private static final Pattern SRC_UNIT_PTN = Pattern.compile(" *(?:FD: *)?(\\d\\d\\b|(?:Affton|Brentwood|City of St Loui|Crestwood FD|Eureka|Fenton|Fenton FPD|Franklin|Jefferson|Kirkwood|Ladue|Lemay|Mehlville|Olivette|Shrewsbury|St Charles|St Louis City|St Louis|Webster Groves)(?: FPD)?)(?: *(?:\\[ ?|Units:)? *((?:(?:(?:STL )?[A-Za-z0-9]+|AB \\d+|GTWY \\d+|\\d+ DUTY|MedicOne \\d|METRO AIR|NEED (?:A|AMB|EMS|MED)(?: \\d+)?|Rescue Dut)\\b,?)+))?$");
  private static final Pattern BAD_AT_PTN = Pattern.compile("(.*?[a-z ])AT(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (BAD_MSG_PTN.matcher(body).matches()) return false;

    body = stripFieldEnd(body, "AT:");

    Matcher match = RESPONDING_PTN.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("2");
      data.strUnit = match.group(1).replace(' ', '_');
      data.strSupp = match.group(2);
      body = body.substring(match.end());
      return super.parseMsg(body, data);
    }

    setSelectValue("1");

    // Strip off leading comment
    if (body.startsWith("Comment:")) {
      int pt = body.indexOf(" AT:");
      if (pt < 0) {
        match = AT_PTN.matcher(body);
        if (!match.find(8)) return false;
        pt =  match.start();
      }
      pt = body.lastIndexOf(",", pt);
      if (pt < 0) return false;
      data.strSupp = body.substring(8,pt).trim();
      body = body.substring(pt+1).trim();
    }

    // Check for a duplicated alert message and remove the duplicates
    int pt = body.indexOf(" AT:");
    if (pt >= 0) {
      int pt2 = body.indexOf(body.substring(0, pt+3), pt+3);
      if (pt2 >= 0) body = body.substring(0,pt2).trim();
    }

    // Occasionally calls are duplicated and we need to cut out the duplicated content :(
    match = DUP_ALERT_PTN.matcher(body);
    if (match.matches())  body = match.group(1) + match.group(2);

    // Channels tend to show up in the middle of things
    // So we will start by identifying and getting rid of them
    match = CHANNEL_PTN.matcher(body);
    if (match.find()) {
      data.strChannel = match.group(1);
      body = append(body.substring(0,match.start()).trim(), " ", body.substring(match.end()).trim());
    }

    match =  ID_PTN.matcher(body);
    if (match.find()) {
      data.strCallId = match.group(1);
      body = body.substring(0,match.start());
    }

    match =  TIME_PTN.matcher(body);
    if (match.find()) {
      data.strTime = match.group(1);
      body = body.substring(0,match.start());
    }

    match = MAP_PTN.matcher(body);
    if (match.find()) {
      data.strMap = match.group(1);
      body = body.substring(0, match.start());
    }

    match = LAT_LONG_PTN.matcher(body);
    if (match.find()) {
      if (match.group(1) != null) {
        setGPSLoc(match.group(1)+'.'+match.group(2)+','+match.group(3)+'.'+match.group(4), data);
      }
      body = body.substring(0,match.start()).trim();
    }

    if ((match = UNIT_SRC_PTN.matcher(body)).find()) {
      data.strUnit =  match.group(1).replace(' ', ',');
      data.strSource = getOptGroup(match.group(2));
    } else if ((match = SRC_UNIT_PTN.matcher(body)).find()) {
      data.strSource = match.group(1);
      data.strUnit = getOptGroup(match.group(2)).replace(' ', '_');
    } else return false;
    body = body.substring(0,match.start()).trim();

    // Make sure there is a blank in front of the AT keyword
    // And a colon behind it
    pt = body.indexOf("AT:");
    if (pt >= 0) {
      body = body.substring(0,pt) + ' ' + body.substring(pt);
    } else if (substring(body, 30,32).equals("AT")) {
      body = body.substring(0,30) + " AT:" + body.substring(32);
    } else {
      pt = body.indexOf(" BUS:");
      if (pt < 0) pt = body.indexOf(" XST:");
      if (pt < 0) pt = body.length();
      match = BAD_AT_PTN.matcher(body.substring(0,pt));
      if (!match.matches()) return false;
      body = match.group(1) + " AT:" + match.group(2) + body.substring(pt);
    }

    body = body.replace("TAC:", " TAC:");

    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "INFO? " + super.getProgram() + " CH SRC UNIT GPS MAP TIME ID";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT_RESP"))  return new MyUnitResponseField();
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(\\d{2}(?:[A-Z]\\d)?) +(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {

      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      field = stripFieldEnd(field, " QD");
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyUnitResponseField extends SkipField {

    @Override
    public String getFieldNames() {
      return "UNIT INFO";
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    return DE_MERVILLE_PTN.matcher(addr).replaceAll("$1$2");
  }
  private static final Pattern DE_MERVILLE_PTN = Pattern.compile("\\b(De) +(Merville)\\b", Pattern.CASE_INSENSITIVE);
}
