using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassLibrary.BusinessEntities
{
    public class User
    {
        public int id { get; set; }
        public string login { get; set; }
        public string password { get; set; }
        public string email { get; set; }
        public DateTime registerDate { get; set; }
        public double points { get; set; }

        public User(int id, string login, string password, string email, DateTime registerDate, double points)
        {
            this.id = id;
            this.login = login;
            this.password = password;
            this.email = email;
            this.registerDate = registerDate;
            this.points = points;
        }

        public User()
        {
        }
    }
}
