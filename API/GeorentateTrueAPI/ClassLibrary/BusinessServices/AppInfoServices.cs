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
    public class AppInfoServices : IAppInfoServices
    {
        private string connectionString;

        public AppInfoServices()
        {
            connectionString = ClassLibrary.Properties.Settings.Default.ConnectionString;
        }

        private SqlDataReader reader;

        public void Add(AppInfo appInfo)
        {
            using(SqlConnection con = new SqlConnection(this.connectionString))
            {
                try
                {
                    con.Open();

                    SqlTransaction tr = con.BeginTransaction();

                    SqlCommand cmd = new SqlCommand("insert into AppInfo(PrivacyPolicy, About, Conditions, FBLink, WebLink) values(@PrivacyPolicy, @About, @Conditions, @FBLink, @WebLink)", con, tr);
                    cmd.CommandType = CommandType.Text;
                    cmd.Parameters.AddWithValue("@PrivacyPolicy", appInfo.PrivacyPolicy);
                    cmd.Parameters.AddWithValue("@About", appInfo.About);
                    cmd.Parameters.AddWithValue("@Conditions", appInfo.Conditions);
                    cmd.Parameters.AddWithValue("@FBLink", appInfo.FBLink);
                    cmd.Parameters.AddWithValue("@WebLink", appInfo.WebLink);

                    cmd.ExecuteNonQuery();
                    tr.Commit();
                }

                catch(Exception e)
                {
                    throw new Exception(e.Message);
                }
            }
        }

        public void Update(AppInfo appInfo)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                try
                {
                    con.Open();

                    SqlTransaction tr = con.BeginTransaction();

                    SqlCommand cmd = new SqlCommand("update AppInfo set PrivacyPolicy = @PrivacyPolicy, About = @About, Conditions = @Conditions, FBLink = @FBLink, WebLink = @WebLink", con, tr);
                    cmd.CommandType = CommandType.Text;
                    cmd.Parameters.AddWithValue("@PrivacyPolicy", appInfo.PrivacyPolicy);
                    cmd.Parameters.AddWithValue("@About", appInfo.About);
                    cmd.Parameters.AddWithValue("@Conditions", appInfo.Conditions);
                    cmd.Parameters.AddWithValue("@FBLink", appInfo.FBLink);
                    cmd.Parameters.AddWithValue("@WebLink", appInfo.WebLink);

                    cmd.ExecuteNonQuery();
                    tr.Commit();
                }

                catch (Exception e)
                {
                    throw new Exception(e.Message);
                }
            }
        }

        public AppInfo Get()
        {
            using(SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                SqlCommand cmd = new SqlCommand("select * from AppInfo", con);
                cmd.CommandType = CommandType.Text;

                reader = cmd.ExecuteReader();

                AppInfo appInfo = new AppInfo();

                while (reader.Read())
                {
                    appInfo.PrivacyPolicy = reader.GetString(0);
                    appInfo.About = reader.GetString(1);
                    appInfo.Conditions = reader.GetString(2);
                    appInfo.FBLink = reader.GetString(3);
                    appInfo.WebLink = reader.GetString(4);
                }

                reader.Close();

                return appInfo;
            }         
        }
    }
}
