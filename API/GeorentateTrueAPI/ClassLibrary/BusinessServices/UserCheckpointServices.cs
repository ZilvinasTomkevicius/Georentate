using ClassLibrary.BusinessEntities;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassLibrary.BusinessServices
{
    public class UserCheckpointServices : IUserCheckpointServices
    {
        private string connectionString;

        public UserCheckpointServices()
        {
            connectionString = ClassLibrary.Properties.Settings.Default.ConnectionString;
        }

        private SqlDataReader reader;

        public void Add(List<UserCheckpoint> checkpointList)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                try
                {
                    con.Open();

                    foreach (UserCheckpoint checkpoint in checkpointList)
                    {                       
                        SqlTransaction tr = con.BeginTransaction();

                        SqlCommand cmd = new SqlCommand("insert into UserCheckpoint (UserID, CheckpointID, Completed) values (@UserID, @CheckpointID, @Completed)", con, tr);
                        cmd.CommandType = CommandType.Text;
                        cmd.Parameters.AddWithValue("@UserID", checkpoint.UserID);
                        cmd.Parameters.AddWithValue("@CheckpointID", checkpoint.CheckpointID);
                        cmd.Parameters.AddWithValue("@Completed", checkpoint.Completed);

                        cmd.ExecuteNonQuery();

                        tr.Commit();
                    }                  
                }
                catch (Exception e)
                {
                    throw new Exception(e.Message);
                }
            }
        }

        public void Update(UserCheckpoint checkpoint)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                try
                {
                    con.Open();

                    SqlTransaction tr = con.BeginTransaction();

                    SqlCommand cmd = new SqlCommand("update UserCheckpoint set Completed = @Completed where UserID = @UserID and CheckpointID = @CheckpointID", con, tr);
                    cmd.CommandType = CommandType.Text;
                    cmd.Parameters.AddWithValue("@UserID", checkpoint.UserID);
                    cmd.Parameters.AddWithValue("@CheckpointID", checkpoint.CheckpointID);
                    cmd.Parameters.AddWithValue("@Completed", checkpoint.Completed);

                    cmd.ExecuteNonQuery();

                    tr.Commit();
                }
                catch (Exception e)
                {
                    throw new Exception(e.Message);
                }
            }
        }

        public void Delete(int UserID, int CheckpointID)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                SqlCommand cmd = new SqlCommand("delete from UserCheckpoint where UserID = @UserID and CheckpointID = @CheckpointID", con);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.AddWithValue("@UserID", UserID);
                cmd.Parameters.AddWithValue("@CheckpointID", CheckpointID);

                cmd.ExecuteNonQuery();
            }
        }

        public UserCheckpoint Get(int UserID, int CheckpointID)
        {

            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                SqlCommand cmd = new SqlCommand("select UserID, CheckpointID, Completed from UserCheckpoint where UserID = @UserID and CheckpointID = @CheckpointID", con);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.AddWithValue("@UserID", UserID);
                cmd.Parameters.AddWithValue("@CheckpointID", CheckpointID);

                reader = cmd.ExecuteReader();

                UserCheckpoint checkpoint = new UserCheckpoint();

                while (reader.Read())
                {
                    checkpoint.UserID = reader.GetInt32(0);
                    checkpoint.CheckpointID = reader.GetInt32(1);
                    checkpoint.Completed = reader.GetBoolean(2);                  
                }

                reader.Close();

                return checkpoint;
            }

        }

        public List<UserCheckpoint> GetList(int id)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                List<UserCheckpoint> checkpointList = new List<UserCheckpoint>();

                SqlCommand cmd = new SqlCommand("select UserID, CheckpointID, Completed from UserCheckpoint where UserID = @ID", con);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.AddWithValue("@ID", id);

                reader = cmd.ExecuteReader();

                while (reader.Read())
                {
                    UserCheckpoint checkpoint = new UserCheckpoint();

                    checkpoint.UserID = reader.GetInt32(0);
                    checkpoint.CheckpointID = reader.GetInt32(1);
                    checkpoint.Completed = reader.GetBoolean(2);

                    checkpointList.Add(checkpoint);
                }

                reader.Close();

                return checkpointList;
            }
        }

        /*
        public int GetCount()
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                int count = 0;

                SqlCommand cmd = new SqlCommand("select count(*) from [Checkpoint]", con);
                cmd.CommandType = CommandType.Text;

                reader = cmd.ExecuteReader();

                while (reader.Read())
                {
                    count = reader.GetInt32(0);
                }

                reader.Close();

                return count;
            }
        }
        */
    }
}
