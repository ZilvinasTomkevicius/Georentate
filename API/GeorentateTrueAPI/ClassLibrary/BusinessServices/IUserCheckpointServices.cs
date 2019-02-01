using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ClassLibrary.BusinessEntities;

namespace ClassLibrary.BusinessServices
{
    public interface IUserCheckpointServices
    {
        void Add(List<UserCheckpoint> checkpointList);
        void Update(UserCheckpoint checkpoint);
        void UpdateList(List<UserCheckpoint> userCheckpoints);
        void Delete(int udi, int cid);
        UserCheckpoint Get(int udi, int cid);
        List<UserCheckpoint> GetList(int id);
    }
}
