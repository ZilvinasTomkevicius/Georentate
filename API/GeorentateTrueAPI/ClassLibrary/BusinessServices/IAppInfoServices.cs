using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ClassLibrary.BusinessEntities;
using ClassLibrary.BusinessServices;

namespace ClassLibrary.BusinessServices
{
    public interface IAppInfoServices
    {
        void Add(AppInfo appInfo);
        void Update(AppInfo appInfo);
        AppInfo Get();
    }
}
