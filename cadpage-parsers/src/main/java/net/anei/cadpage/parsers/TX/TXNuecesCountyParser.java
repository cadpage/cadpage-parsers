package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;


public class TXNuecesCountyParser extends SmartAddressParser {

  public TXNuecesCountyParser() {
    super(CITY_CODES, "NUECES COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "mispublicsafety@cctexas.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern MASTER = Pattern.compile("Unit (\\S+) was (cleared from|dispatched to) event (\\d+) at (.*?) NUECES (?:\\| (.*?) )?for (.*)");
  private static final Pattern MASTER2 = Pattern.compile("Event (\\d+) \\((.*?)\\) at (.*?) NUECES (?:\\| (.*) )?was created");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Device Notification")) return false;
    int pt = body.indexOf("|MESS Unit ");
    if (pt >= 0) {
      body = body.substring(pt+6).trim();
      body = stripFieldEnd(body, " .|SEND|QUIT");
    } else {
      pt = body.indexOf("Message:\t");
      if (pt < 0) return false;
      body = body.substring(pt+9);
      body = stripFieldEnd(body, ".");
      body = body + " for ALERT";
    }

    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID ADDR APT CITY PLACE CALL");
      data.strUnit = match.group(1);
      if (match.group(2).startsWith("cleared")) data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(3);
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(4).trim(), data);
      data.strPlace = getOptGroup(match.group(5));
      data.strCall = match.group(6).trim();
    }

    else {
      body = stripFieldEnd(body, " for ALERT");
      if ((match = MASTER2.matcher(body)).matches()) {
        setFieldList("ID CALL ADDR APT CITY PLACE");
        data.strCallId = match.group(1);
        data.strCall = match.group(2).trim();
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(3).trim(), data);
        data.strPlace = getOptGroup(match.group(4));
      }

      else {
        setFieldList("INFO");
        data.msgType = MsgType.GEN_ALERT;
        data.strSupp = body;
      }
    }
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BANQ",   "BANQUETE",
      "BISH",   "BISHOP",
      "CC",     "CORPUS CHRISTI",
      "KLEBG",  "KLEBERG COUNTY",
      "OCL",    "",  // ???
      "ROBS",   "ROBSTOWN",
      "TIER",   "TIERRA GRANDE"
  });}
