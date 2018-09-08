using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassLibrary.BusinessEntities
{
    public class User
    {
        public int ID { get; set; }
        public string Login { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }
        public DateTime RegisterDate { get; set; }
        public int Points { get; set; }

        public User(int id, string login, string password, string email, DateTime registerDate, int points)
        {
            ID = id;
            Login = login;
            Password = password;
            Email = email;
            RegisterDate = registerDate;
            Points = points;
        }

        public User()
        {
        }
    }
}
