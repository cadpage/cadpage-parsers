package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA90Parser;

public class KYFortKnoxParser extends DispatchA90Parser {

  public KYFortKnoxParser() {
    super("FORT KNOX", "KY");
  }

  @Override
  public String getFilter() {
    return "monacoenterprises2014@gmail.com,fkfd469@gmail.com";
  }
}
