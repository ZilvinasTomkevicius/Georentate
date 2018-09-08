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
    public class CheckpointServices : ICheckpointServices
    {
        private string connectionString;

        public CheckpointServices()
        {
            connectionString = ClassLibrary.Properties.Settings.Default.ConnectionString;
        }

        private SqlDataReader reader;

        public void Add(Checkpoint checkpoint)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                try
                {
                    con.Open();

                    SqlTransaction tr = con.BeginTransaction();

                    SqlCommand cmd = new SqlCommand("insert into [Checkpoint]([Name], Scan, Hint, Latitude, Longitude, Points) values (@Name, @Scan, @Hint, @Latitude," +
                        "@Longitude, @Points)", con, tr);
                    cmd.CommandType = CommandType.Text;
                    cmd.Parameters.AddWithValue("@Name", checkpoint.Name);
                    cmd.Parameters.AddWithValue("@Scan", checkpoint.Scan);
                    cmd.Parameters.AddWithValue("@Hint", checkpoint.Hint);
                    cmd.Parameters.AddWithValue("@Latitude", checkpoint.Latitude);
                    cmd.Parameters.AddWithValue("@Longitude", checkpoint.Longitude);
                    cmd.Parameters.AddWithValue("@Points", checkpoint.Points);

                    cmd.ExecuteNonQuery();

                    tr.Commit();
                }
                catch (Exception e)
                {
                    throw new Exception(e.Message);
                }
            }
        }

        public void Update(Checkpoint checkpoint)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                try
                {
                    con.Open();

                    SqlTransaction tr = con.BeginTransaction();

                    SqlCommand cmd = new SqlCommand("update [Checkpoint] set [Name] = @Name, Scan = @Scan, Hint = @Hint, Latitude = @Latitude, " +
                        "Longitude = @Longitude, Points = @Points where ID = @ID", con, tr);
                    cmd.CommandType = CommandType.Text;
                    cmd.Parameters.AddWithValue("@ID", checkpoint.ID);
                    cmd.Parameters.AddWithValue("@Name", checkpoint.Name);
                    cmd.Parameters.AddWithValue("@Scan", checkpoint.Scan);
                    cmd.Parameters.AddWithValue("@Hint", checkpoint.Hint);
                    cmd.Parameters.AddWithValue("@Latitude", checkpoint.Latitude);
                    cmd.Parameters.AddWithValue("@Longitude", checkpoint.Longitude);
                    cmd.Parameters.AddWithValue("@Points", checkpoint.Points);

                    cmd.ExecuteNonQuery();

                    tr.Commit();
                }
                catch (Exception e)
                {
                    throw new Exception(e.Message);
                }
            }
        }

        public void Delete(int ID)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                SqlCommand cmd = new SqlCommand("delete from [Checkpoint] where ID = @ID", con);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.AddWithValue("@ID", ID);

                cmd.ExecuteNonQuery();
            }
        }

        public Checkpoint Get(int ID)
        {

            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                SqlCommand cmd = new SqlCommand("select ID, Name, Scan, Hint, Latitude, Longitude, Points from [Checkpoint] where ID = @ID", con);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.AddWithValue("@ID", ID);

                reader = cmd.ExecuteReader();

                Checkpoint checkpoint = new Checkpoint();

                while (reader.Read())
                {
                    checkpoint.ID = reader.GetInt32(0);
                    checkpoint.Name = reader.GetString(1);
                    checkpoint.Scan = reader.GetString(2);
                    checkpoint.Hint = reader.GetString(3);
                    checkpoint.Latitude = reader.GetDouble(4);
                    checkpoint.Longitude = reader.GetDouble(5);
                    checkpoint.Points = reader.GetInt32(6);
                }

                reader.Close();

                return checkpoint;
            }

        }

        public List<Checkpoint> GetList()
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                List<Checkpoint> checkpointList = new List<Checkpoint>();

                SqlCommand cmd = new SqlCommand("select ID, Name, Scan, Hint, Latitude, Longitude, Points from [Checkpoint]", con);
                cmd.CommandType = CommandType.Text;

                reader = cmd.ExecuteReader();

                while (reader.Read())
                {
                    Checkpoint checkpoint = new Checkpoint();

                    checkpoint.ID = reader.GetInt32(0);
                    checkpoint.Name = reader.GetString(1);
                    checkpoint.Scan = reader.GetString(2);
                    checkpoint.Hint = reader.GetString(3);
                    checkpoint.Latitude = reader.GetDouble(4);
                    checkpoint.Longitude = reader.GetDouble(5);
                    checkpoint.Points = reader.GetInt32(6);

                    checkpointList.Add(checkpoint);
                }

                reader.Close();

                return checkpointList;
            }
        }

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

        /*
        public List<CheckpointEntity> GetCheckpointsForGame(GameIDEntity gameID)
        {
            using (SqlConnection con = new SqlConnection(this.connectionString))
            {
                con.Open();

                List<CheckpointEntity> checkpointList = new List<CheckpointEntity>();

                DataTable dataTable = new DataTable("Temp_gameID");

                dataTable.Columns.Add("GameId", typeof(int));

                dataTable.Rows.Add(gameID.gameID);

                SqlCommand cmd = new SqlCommand("GetGameCheckpoints", con);
                cmd.CommandType = CommandType.StoredProcedure;

                SqlParameter parameter = cmd.Parameters.AddWithValue("@tg_id", dataTable);
                parameter.SqlDbType = SqlDbType.Structured;

                cmd.ExecuteNonQuery();

                reader = cmd.ExecuteReader();

                while (reader.Read())
                {
                    CheckpointEntity checkpoint = new CheckpointEntity();

                    checkpoint.ID = reader.GetInt32(0);
                    checkpoint.Name = reader.GetString(1);
                    checkpoint.ScanString = reader.GetString(2);
                    checkpoint.Latitude = reader.GetDouble(3);
                    checkpoint.Longitude = reader.GetDouble(4);
                    checkpoint.Points = reader.GetInt32(5);

                    checkpointList.Add(checkpoint);
                }

                reader.Close();

                return checkpointList;
            }
        }
        */
    }
}
