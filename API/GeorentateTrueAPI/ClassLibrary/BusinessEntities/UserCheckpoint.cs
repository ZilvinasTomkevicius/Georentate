using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassLibrary.BusinessEntities
{
    public class UserCheckpoint
    {
        public int UserID { get; set; }
        public int CheckpointID { get; set; }
        public bool Completed { get; set; }

        public UserCheckpoint(int userid, int checkpointid, bool completed)
        {
            UserID = userid;
            CheckpointID = checkpointid;
            Completed = completed;
        }

        public UserCheckpoint()
        {
        }
    }
}
