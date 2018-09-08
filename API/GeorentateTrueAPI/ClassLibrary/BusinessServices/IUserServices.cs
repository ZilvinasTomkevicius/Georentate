using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ClassLibrary.BusinessEntities;

namespace ClassLibrary.BusinessServices
{
    public interface IUserServices
    {
        int Add(User user);
        void Update(User user);
        void Delete(int id);
        User Get(int id);
        User LoggingOnUser(User user);
        List<User> GetList();
    }
}
