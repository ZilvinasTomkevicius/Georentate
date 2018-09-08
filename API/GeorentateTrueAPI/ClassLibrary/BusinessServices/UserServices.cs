using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ClassLibrary.BusinessEntities;

namespace ClassLibrary.BusinessServices
{
    public class UserServices : IUserServices
    {
        private string connectionString;

        public UserServices()
        {
            connectionString = ClassLibrary.Properties.Settings.Default.ConnectionString;
        }

        private SqlDataReader reader;

        public int Add(User user)
        {
            int id = 0;

            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                try
                {
                    con.Open();

                    SqlTransaction tr = con.BeginTransaction();

                    SqlCommand cmd = new SqlCommand("insert into [User](Login, Password, Email, Points) values (@Login, @Password, @Email, @Points)", con, tr);
                    cmd.CommandType = CommandType.Text;
                    cmd.Parameters.AddWithValue("@Login", user.Login);
                    cmd.Parameters.AddWithValue("@Password", user.Password);
                    cmd.Parameters.AddWithValue("@Email", user.Email);
                    cmd.Parameters.AddWithValue("@Points", 0);

                    cmd.ExecuteNonQuery();                
                    tr.Commit();

                    SqlCommand cmd2 = new SqlCommand("select ID, Login, Password, Email, RegisterDate, Points from [User] where [Login] = @Login", con);
                    cmd2.CommandType = CommandType.Text;
                    cmd2.Parameters.AddWithValue("@Login", user.Login);

                    reader = cmd2.ExecuteReader();

                    while (reader.Read())
                    {
                        id = reader.GetInt32(0);                      
                    }

                    reader.Close();
                }
                catch (Exception e)
                {
                    throw new Exception(e.Message);
                }

                return id;
            }
         
        }

        public void Update(User user)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                try
                {
                    con.Open();

                    SqlTransaction tr = con.BeginTransaction();

                    SqlCommand cmd = new SqlCommand("update [User] set Login = @Login, Password=@Password, Email = @Email, Points = @Points where ID = @ID", con, tr);
                    cmd.CommandType = CommandType.Text;
                    cmd.Parameters.AddWithValue("@Login", user.Login);
                    cmd.Parameters.AddWithValue("@Password", user.Password);
                    cmd.Parameters.AddWithValue("@Email", user.Email);
                    cmd.Parameters.AddWithValue("@Points", user.Points);
                    cmd.Parameters.AddWithValue("@ID", user.ID);

                    cmd.ExecuteNonQuery();

                    tr.Commit();
                }
                catch (Exception e)
                {
                    throw new Exception(e.Message);
                }
            }
        }

        public void Delete(int userID)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                SqlCommand cmd = new SqlCommand("delete from [User] where ID = @ID", con);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.AddWithValue("@Id", userID);

                cmd.ExecuteNonQuery();
            }
        }

        public User Get(int userID)
        {
           using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                SqlCommand cmd = new SqlCommand("select ID, Login, Password, Email, RegisterDate, Points from [User] where ID = @ID", con);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.AddWithValue("@ID", userID);

                reader = cmd.ExecuteReader();

                User user = new User();

                while (reader.Read())
                {
                    user.ID = reader.GetInt32(0);
                    user.Login = reader.GetString(1);
                    user.Password = reader.GetString(2);
                    user.Email = reader.GetString(3);
                    user.RegisterDate = reader.GetDateTime(4);
                    user.Points = reader.GetInt32(5);
                }

                reader.Close();

                return user;
            }
        }
       
        public List<User> GetList()
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                List<User> userList = new List<User>();

                SqlCommand cmd = new SqlCommand("select ID, Login, Password, Email, RegisterDate, Points from [User]", con);
                cmd.CommandType = CommandType.Text;

                reader = cmd.ExecuteReader();

                while (reader.Read())
                {
                    User user= new User();

                    user.ID = reader.GetInt32(0);
                    user.Login = reader.GetString(1);
                    user.Password = reader.GetString(2);
                    user.Email = reader.GetString(3);
                    user.RegisterDate = reader.GetDateTime(4);
                    user.Points = reader.GetInt32(5);

                    userList.Add(user);
                }

                reader.Close();

                return userList;
            }
        }      

        public User LoggingOnUser(User user)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                User user1 = new User();

                DataTable dataTable = new DataTable("Temp_User");

                dataTable.Columns.Add("Login", typeof(string));
                dataTable.Columns.Add("Password", typeof(string));

                dataTable.Rows.Add(user.Login, user.Password);

                SqlCommand cmd = new SqlCommand("Logging_On_User", con);
                cmd.CommandType = CommandType.StoredProcedure;

                SqlParameter parameter = cmd.Parameters.AddWithValue("@tu", dataTable);
                parameter.SqlDbType = SqlDbType.Structured;

                cmd.ExecuteNonQuery();

                reader = cmd.ExecuteReader();

                while (reader.Read())
                {
                    user1.ID = reader.GetInt32(0);
                    user1.Login = reader.GetString(1);
                    user1.Password = reader.GetString(2);
                    user1.Email = reader.GetString(3);
                    user1.RegisterDate = reader.GetDateTime(4);
                    user1.Points = reader.GetInt32(5);
                }

                reader.Close();

                return user1;
            }
        }
    }
}
