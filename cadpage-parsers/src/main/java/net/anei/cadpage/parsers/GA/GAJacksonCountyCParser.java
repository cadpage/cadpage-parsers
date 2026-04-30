package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class GAJacksonCountyCParser extends DispatchH05Parser {

  public GAJacksonCountyCParser() {
    super("JACKSON COUNTY", "GA",
          "Address:ADDRCITY! Date/Time:SKIP! Call_Taker:SKIP! Caller_Name:NAME! Caller_Address:SKIP! Cross_Streets:X! " +
              "EMS_Call_Type_Description:CALL! EMS_Call_Type_Name:CODE! Dispatch_Date/Time:DATETIME! Maps_Hyperlink:EMPTY! https:GPS! " +
              "Incident_#:ID! Narrative:EMPTY! INFO_BLK+ Status_Times:EMPTY! TIMES+");
    setAccumulateUnits(true);
  }

  @Override
  public String getFilter() {
    return "cadpage@jacksoncountygov.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.equals("Text Message")) {
      if (!body.startsWith("Automatic R&R Notification")) return false;
      int pt = body.indexOf('\n');
      subject = body.substring(0,pt).trim();
      body = body.substring(pt+1).trim();
    }

    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new GPSField("//www.google.com/maps/search.*query=(.*)", true);
    return super.getField(name);
  }
}
