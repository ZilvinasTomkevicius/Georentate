using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassLibrary.BusinessEntities
{
    public class AppInfo
    {
        public string PrivacyPolicy { get; set; }
        public string About { get; set; }
        public string Conditions { get; set; }
        public string FBLink { get; set; }
        public string WebLink { get; set; }

        public AppInfo(string privacyPolicy, string about, string conditions, string fblink, string weblink)
        {
            PrivacyPolicy = privacyPolicy;
            About = about;
            Conditions = conditions;
            FBLink = fblink;
            WebLink = weblink;
        }

        public AppInfo()
        {
        }
    }
}
