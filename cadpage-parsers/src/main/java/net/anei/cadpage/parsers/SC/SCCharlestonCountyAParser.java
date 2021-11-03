package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class SCCharlestonCountyAParser extends FieldProgramParser {

  public SCCharlestonCountyAParser() {
    super("CHARLESTON COUNTY", "SC",
          "( Comment:INFO ( Problem:CALL! Address:ADDR! X_Street:X! " +
                         "| IN:ID! PN:CALL! IC:EMPTY! PR:APT! AD:ADDR! CS:X! CT:CITY! BD:EMPTY! AN:SKIP! ) " +
          "| PREFIX Address:ADDR! X_Street:X Cmd_Channel:CH% " +
          "| ADDR2/SC! X_Street:X Cmd_Channel:CH! Units_Assigned:UNIT% Time:TIME " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "@charlestoncounty.org,8573031986,2183500260";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // Strip off text msg heading
    if (subject.length() == 0) {
      body = stripFieldStart(body, "/ ");
      if (body.startsWith("Dispatch Info / ")) {
        subject = "Dispatch Info";
        body = body.substring(16).trim();
      }
      else if (body.startsWith("CHARLESTON COUNTY: ")) {
        subject = "Dispatch Info";
        body = body.substring(19).trim();
      }
    }

    // See if we can parse this as a fixed field message
    if (!parseFixedFieldMsg(subject, body, data)) {

      // No luck, try it as a variable length field message
      body = body.replace(" Op Channel:", " Cmd Channel:")
                 .replace(" Cmnd Channel:", " Cmd Channel:")
                 .replace(" X Streets:", " X Street:")
                 .replace(",Problem:", " Problem:")
                 .replace(",IN:", " IN:")
                 .replace("IC:", " IC:");
      if (! super.parseMsg(body, data)) return false;
    }
    if (data.msgType == MsgType.GEN_ALERT) return true;
    if (data.strCall.length() == 0) return false;
    if (data.strAddress.length() == 0 && !data.strCall.equals("Times")) return false;
    data.strChannel = stripFieldStart(data.strChannel, "_");
    return true;
  }

  private boolean parseFixedFieldMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Info")) return false;

    do {
      FParser p = new FParser(body);

      if (p.checkAhead(93, "Cmd Chan:")) {
        setFieldList("UNIT ID CALL ADDR APT CH");
        data.strUnit = p.get(10);
        data.strCallId = p.get(13);
        data.strCall = stripFieldStart(p.get(30), "*");
        parseAddress(p.get(40), data);
        if (!p.check("Cmd Chan:")) return false;
        data.strChannel = p.get();
        return true;
      }

      if (p.checkAhead(20, "DPC:")) {
        setFieldList("CALL UNIT ID INFO");
        data.strCall = "Times";
        data.strUnit = p.get(9);
        data.strCallId = p.get(11);
        parseLabeledField(p.get(4), p.get(5), data);
        for (int ndx = 1; ndx <= 7; ndx++) {
          String key = p.get(ndx < 7 ? 4 : 6);
          String value = p.get(8);
          if (parseLabeledField(key, value, data) && ndx >= 6) data.msgType = MsgType.RUN_REPORT;
        }
        return true;
      }

      if (p.checkAhead(20, "Unit:")) {
        setFieldList("ID UNIT INFO ADDR APT");
        data.msgType = MsgType.GEN_ALERT;
        data.strCallId = p.get(20);
        if (!p.check("Unit:")) return false;
        data.strUnit = p.get(10);
        data.strSupp = p.get(64);
        if (!p.check("Address:")) return false;
        parseAddress(p.get(), data);
        return true;
      }

      if (p.checkAhead(116, " U:")) {
        setFieldList("CALL APT ADDR X GPS UNIT");
        data.strCall = p.get(19);
        String apt = p.get(15);
        parseAddress(p.get(30), data);
        data.strApt = append(data.strApt, "-", apt);
        data.strCross = p.get(30);
        String gps1 = p.get(8);
        if (!p.check("   / ")) return false;
        String gps2 = p.get(8);
        setGPSLoc(cvtGpsCoord(gps1)+','+cvtGpsCoord(gps2), data);
        if (!p.check("  U:")) return false;
        data.strUnit = p.get();
        return true;
      }

      String call = stripFieldStart(p.get(30), "*");
      if (p.check(" ")) break;
      String addr = p.get(40);


      String cross = "", channel, unit;
      if (p.check("X Streets:")) {
        cross = p.get(40);
        p.setOptional();
        if (!p.check("Cmd Channel:")) break;
        channel = p.get(30);
        if (!p.check("Unit Assigned:")) break;
        unit = p.get();
      } else if (p.check("X St:")) {
        cross = p.get(30);
        p.setOptional();
        if (!p.check("Cmd Chan:")) break;
        channel = stripFieldStart(p.get(15), "_");
        if (!p.check("Units:")) break;
        unit = p.get();
      } else if (p.check("C:")) {
        channel = stripFieldStart(p.get(15), "_");
        if (!p.check("U:")) break;
        unit = p.get();
      } else break;

      setFieldList("CALL ADDR APT X CH UNIT");
      data.strCall = call;
      parseAddress(addr, data);
      data.strCross = cross;
      data.strChannel = channel;
      data.strUnit = unit;
      if (body.length() < 176) data.expectMore = true;
      return true;
    } while (false);

    FParser p = new FParser(body);
    if (p.checkAhead(34, "District ")){
      String unit = p.get(14);
      String id = p.get(20);
      if (!p.check("District ")) return false;
      String source = p.get(3);
      String call = p.get(8);
      String code = p.get(10);
      String addr = p.get(20);
      if (!p.check("XS:")) return false;
      String cross = p.get(34);
      p.setOptional();
      if (!p.check("Apt/Bldg:")) return false;
      String apt = p.get(13);
      String info = p.get(30);
      if (!p.check("Location Name:")) return false;
      String place = p.get();

      setFieldList("UNIT ID SRC CALL CODE ADDR X APT INFO PLACE");
      data.strUnit = unit;
      data.strCallId = id;
      data.strSource = source;
      data.strCall = call;
      data.strCode = code;
      parseAddress(addr, data);
      data.strCross = cross;
      data.strApt = append(data.strApt, "-", apt);
      data.strSupp = info;
      data.strPlace = place;
      if (body.length() < 130) data.expectMore = true;
      return true;
    }

    return false;
  }

  private String cvtGpsCoord(String gps) {
    int pt = gps.length() - 6;
    if (pt < 0) return gps;
    return gps.substring(0,pt)+'.'+gps.substring(pt);
  }

  private boolean parseLabeledField(String key, String value, Data data) {
    if (value.isEmpty()) return false;
    data.strSupp = append(data.strSupp, "\n", key + value);
    return true;
  }

  private static final Pattern PREFIX_PTN =
      Pattern.compile("(\\d{4}-\\d{7}) District (\\d{2}) (.*)");
  private class PrefixField extends CallField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = PREFIX_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = match.group(1);
        data.strSource = match.group(2);
        field =  match.group(3);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ID SRC CALL";
    }
  }

  private static final Pattern RESP_AREA_PTN =
      Pattern.compile("^(NCFD (?:NORTH|SOUTH|EAST|WEST) \\d+|[A-Z]{2}FD \\d+|FD WEST ASHLEY RIVER|CHFD \\d+|(?:ST|TH) NAVAL WEAPONS STA)\\b");
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" Response Area: ");
      if (pt >= 0) {
        String call = field.substring(0,pt).trim();
        field = field.substring(pt+16).trim();
        Matcher match = RESP_AREA_PTN.matcher(field);
        if (match.find()) {
          data.strMap = match.group(1);
          parseAddress(field.substring(match.end()).trim(), data);
        } else {
          super.parse(field, data);
          data.strMap = data.strCall;
        }
        data.strCall = call;
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL MAP ADDR APT";
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PREFIX")) return new PrefixField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    return super.getField(name);
  }
}
