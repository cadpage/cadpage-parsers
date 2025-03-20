package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXSeabrookAParser extends DispatchA18Parser {

  private static final Pattern SUBJECT_SRC_PTN = Pattern.compile("[A-Z]{1,3}FD");

  public TXSeabrookAParser() {
    super(TXSeabrookParser.CITY_LIST, "HARRIS COUNTY","TX");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,jvfire@ci.jersey-village.tx.us";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (SUBJECT_SRC_PTN.matcher(subject).matches()) data.strSource = subject;
    subject = "";
    return super.parseMsg(subject, body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("Northwest Freeway SR", "Northwest Freeway");
  }
}
