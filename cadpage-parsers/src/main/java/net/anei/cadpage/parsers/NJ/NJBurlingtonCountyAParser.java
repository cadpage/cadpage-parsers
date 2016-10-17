package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NJBurlingtonCountyAParser extends FieldProgramParser {
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "Southamptn", "Southampton",
      "Woodland",   "Woodland",
      "Pembtn Twp", "Pemberton Twp",
      "Pembtn Bor", "Pemberton",
      "Eastampton", "Eastampton",
      "Tabernacle", "Tabernacle",
      "MedfordTwp", "Medford Twp",
      "MedfordLks", "Medford Lakes",
      "Lumberton",  "Lumberton",
      "Shamong",    "Shamong"
  });
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("[\\w ]+");
  
  public NJBurlingtonCountyAParser() {
    super(CITY_CODES, "BURLINGTON COUNTY", "NJ",
           "CALL Priority:PRI! ADDR! Venue:CITY! CrossStreet:ADDR2? LocatedBetween:X? PLACE CallTime:TIME ID SKIP PHONE Caller:NAME Complaintant:SKIP NatureOfCall:INFO INFO+ AdditionalInfo:INFO");
  }
  
  @Override
  public String getFilter() {
    return "777,888";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.find()) return false;
    data.strSource = subject;
    
    if (!body.startsWith(": ")) return false;
    body = body.substring(2).trim();
    if (!body.contains("Priority:")) body = "Priority:" + body;
    
    // See if we have all of it
    int pt = body.indexOf("Additional Inc#s");
    if (pt >= 0) {
      body = body.substring(0,pt);
    } else {
      data.expectMore = true;
    }
    
    body = body.replace(" Priority:", "\nPriority:");
    body = body.replace("Venue:", "\nVenue:");
    body = body.replace("Cross Street . :", "CrossStreet:");
    body = body.replace("Located Between  :", "LocatedBetween:");
    body = body.replace("Call Time-", "CallTime:");
    body = body.replace("Caller :", "\nCaller:");
    body = body.replace("Caller  :", "\nCaller:");
    body = body.replace("Nature of Call :", "NatureOfCall:");
    body = body.replace("Additional Info", "AdditionalInfo:");
    body = body.replace(" : ", "\n");
    body = body.replaceAll("\n *:?", "\n");
    pt = body.indexOf("AdditionalInfo");
    if (pt >= 0) {
      body = body.substring(0,pt) + body.substring(pt).replaceAll("\\s+", " ");
    }
    body = body.replaceAll("  +", " ");
    return parseFields(body.split("\n"), data);
  }
  
  private class Address2Field extends Field {
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.contains("&") & data.strAddress.contains(field)) return;
      data.strAddress = append(data.strAddress, " & ", field);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR2")) return new Address2Field();
    return super.getField(name);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
